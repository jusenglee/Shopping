package com.example.weblogin.domain.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    private String category_name;

    private String brand_name;

    private Integer countView;

    private Integer heart;

    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, String brand_name, String category_name, Integer countView, Integer heart) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.brand_name = brand_name;
        this.category_name = category_name;
        this.countView = countView;
        this.heart = heart;
    }
}