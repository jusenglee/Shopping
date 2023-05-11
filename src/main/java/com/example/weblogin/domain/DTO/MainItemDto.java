package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.Kategorie;
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

    private Integer heart;

    private Integer countview;

    private Brand brand;

    private KategorieDto category;

    public MainItemDto(Item item, Kategorie kategorie) {
        this.id = item.getId();
        this.itemNm = item.getItemNm();
        this.price = item.getPrice();
        this.itemDetail = item.getItemDetail();
        this.category = new KategorieDto(kategorie);
    }
}