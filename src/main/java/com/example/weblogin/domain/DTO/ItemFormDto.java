package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.Kategorie;
import com.example.weblogin.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotNull(message = "카테고리 필수 입력 값입니다.")
    private Long category;

    @NotNull(message = "브랜드 필수 입력 값입니다.")
    private Long brand;

    @NotBlank(message = "상품명은 필수 입력 값입니다.x")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    private Member ADMIN;//브랜드 계정

    @NotNull(message = "상품 상태는 필수 입력 값입니다.")
    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();


    private Integer countview;


    private Integer heart;

    public Integer getCountview() {
        return countview;
    }

    public Integer getHeart() {
        return heart;
    }

    @Builder
    public ItemFormDto(Long id, Long category, Long brand, String itemNm, Integer price, String itemDetail, Integer stock, Member ADMIN, ItemSellStatus itemSellStatus, List<ItemImgDto> itemImgDtoList, List<Long> itemImgIds, Integer countview, Integer heart) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stock = stock;
        this.ADMIN = ADMIN;
        this.itemSellStatus = itemSellStatus;
        this.itemImgDtoList = itemImgDtoList;
        this.itemImgIds = itemImgIds;
        this.countview = countview;
        this.heart = heart;
    }

    public Item toEntity(Brand brand, Kategorie category) {
        return Item.builder()
                .itemNm(itemNm)
                .itemDetail(itemDetail)
                .itemSellStatus(itemSellStatus)
                .price(price)
                .stockNumber(stock)
                .category(category)
                .brand(brand)
                .countview(0)
                .heart(0)
                .build();
    }

    public static ItemFormDto of(Item entity) {

        return ItemFormDto.builder()
                .itemNm(entity.getItemNm())
                .itemDetail(entity.getItemDetail())
                .itemSellStatus(entity.getItemSellStatus())
                .price(entity.getPrice())
                .stock(entity.getStockNumber())
                .build();
    }
}
