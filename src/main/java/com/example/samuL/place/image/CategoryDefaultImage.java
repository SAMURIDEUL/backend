package com.example.samuL.place.image;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryDefaultImage {
    CATEGORY1(1L, "/images/default/pharmacy.png"),
    CATEGORY2(2L, "/images/default/art.png"),
    CATEGORY3(3L, "/images/default/cafe.png"),
    CATEGORY4(4L, "/images/default/hotel.png"),
    CATEGORY5(5L, "/images/default/supplies.png"),
    CATEGORY6(6L, "/images/default/beauty.png"),
    CATEGORY7(7L, "/images/default/literary.png"),
    CATEGORY8(8L, "/images/default/pension.png"),
    CATEGORY9(9L, "/images/default/restaurant.png"),
    CATEGORY10(10L, "/images/default/travel.png"),
    CATEGORY11(11L, "/images/default/consignment.png"),
    CATEGORY12(12L, "/images/default/museum.png"),
    CATEGORY13(13L, "/images/default/hotel.png");

    private final Long categoryId;
    private final String defaultImage;

    public static String getDefaultImage(Long categoryId){
        for(CategoryDefaultImage img : values()){
            if(img.categoryId.equals(categoryId)){
                return img.defaultImage;
            }
        }
        return null;
    }
}

