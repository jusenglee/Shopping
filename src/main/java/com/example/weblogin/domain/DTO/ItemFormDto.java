package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.itemCategory.Kategorie;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.KategorieService;
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

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    private Member ADMIN;//브랜드 계정

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private Integer countview;

    private Integer heart;

    @Builder
    public ItemFormDto(Long id,String itemNm, Integer price, String itemDetail, Integer stock, ItemSellStatus itemSellStatus, Member ADMIN,
                       Long category ) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stock = stock;
        this.itemSellStatus = itemSellStatus;
        this.ADMIN = ADMIN;
        this.category = category;
    }

    public Item toEntity(ItemFormDto dto, KategorieService kategorieService) {
        Kategorie category = kategorieService.findKategorieById(dto.category);

        Item entity = Item.builder()
                .itemNm(dto.itemNm)
                .itemDetail(dto.itemDetail)
                .itemSellStatus(dto.itemSellStatus)
                .price(dto.price)
                .stockNumber(dto.stock)
                .category(category)
                .countview(0)
                .heart(0)
                .build();

        return entity;
    }

    public static ItemFormDto of(Item entity) {
        ItemFormDto dto = ItemFormDto.builder()
                .itemNm(entity.getItemNm())
                .itemDetail(entity.getItemDetail())
                .itemSellStatus(entity.getItemSellStatus())
                .price(entity.getPrice())
                .stock(entity.getStockNumber())
                .build();

        return dto;
    }
}
