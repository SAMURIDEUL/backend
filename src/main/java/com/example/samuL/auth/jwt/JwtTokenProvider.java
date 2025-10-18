package com.example.samuL.auth.jwt;



import com.example.samuL.common.exception.jwtAuth.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    //jwt 만료 시간 1시간(1000L * 60 * 60)
    private final long JWT_TOKEN_TIME = 1000L * 60 * 60;
    //refresh token 만료 시간 7일
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7;

    //secret 키
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // access token
    public String CreateToken(String email){
        Date now = new Date(); //토큰 발급 시간
        Date Validity = new Date(now.getTime() + JWT_TOKEN_TIME); // 토큰 만료 시간

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(Validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // refresh token
    public String createRefreshToken(String email){
        Date now = new Date();
        Date expriryDate = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expriryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    //Jwt 토큰에서 사용자 email 추출
    public String extractEmail(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 서비스 로직 전용, 토큰 유효성 체크
    public boolean isValidateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }

    // 필터 전용, 토큰 유효성에 따른 Exception 처리 전용
    public void validateToken(String token) throws JwtAuthenticationException{
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("토큰이 만료되었습니다.", e);
        }catch (MalformedJwtException e) {
            throw new JwtAuthenticationException("토큰 형식이 올바르지 않습니다.", e);
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("토큰 서명이 유효하지 않습니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("지원하지 않는 토큰 형식입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("토큰이 비어있거나 잘못되었습니다.", e);
        }
//        catch (JwtException | IllegalArgumentException e) {
//            throw new JwtAuthenticationException("유효하지 않은 토큰입니다.", e);
//        }
//        Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token);
//        return true;
//        try{
//            Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        }
//        catch(ExpiredJwtException e){
//            return false;
//        }
//        catch(MalformedJwtException e){
//            return false;
//        }
//        catch(JwtException | IllegalArgumentException e){
//            return false;
//        }
    }


    //jwt 토큰 만료시간 추출
    public LocalDateTime getExpiration(String token){
      Claims claims = parseClaims(token);
      Date expiration = claims.getExpiration();
      if (expiration == null){
          throw new IllegalArgumentException("토큰에 만료시간이 포함되지 않았습니다.");
      }
      return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch(ExpiredJwtException e){
            return e.getClaims();

        }catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("유효하지 않은 jwt 토큰입니다.", e);
        }
    }


    //jwt 토큰 resolve
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

}
