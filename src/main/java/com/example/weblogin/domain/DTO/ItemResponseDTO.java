package com.example.weblogin.domain.DTO;

import java.util.List;
import java.util.stream.Collectors;

import com.example.weblogin.domain.item.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {

    private Long id;
    private String itemNm;
    private Integer price;
    private Integer salePer;
    private String itemDetail;
    private String itemSellStatus;

    // 이미지 목록
    private List<ItemImgDTO> itemImgList;

    // 옵션 목록
    private List<ItemOptionDTO> itemOptionList;

    // 카테고리나 브랜드 같은 연관관계 필드는
    // 필요하다면 추가해서, 단순히 ID나 이름 정도만 담는 걸 권장
    private String cateName;
    private String brandName;

    // 상품 등록자(admin)도 노출이 필요하다면 아이디/이름 정도만
    private Long adminId;

    private String createdDate;
    public static ItemResponseDTO toDTO(Item item) {
        if (item == null) {
            return null;
        }

        return ItemResponseDTO.builder()
            .id(item.getId())
            .itemNm(item.getItemNm())
            .price(item.getPrice())
            .salePer(item.getSalePer())
            .itemDetail(item.getItemDetail())
            .itemSellStatus(item.getItemSellStatus() != null ? item.getItemSellStatus().name() : null)
            .itemImgList(
                item.getItemImgs() == null ? null :
                    item.getItemImgs().stream()
                        .map(ItemImgDTO::toDTO)
                        .collect(Collectors.toList())
            )
            .itemOptionList(
                item.getOptions() == null ? null :
                    item.getOptions().stream()
                        .map(ItemOptionDTO::toDTO)
                        .collect(Collectors.toList())
            )
            .cateName(item.getCategory() != null ? item.getCategory().getCateName() : null)
            .brandName(item.getBrand() != null ? item.getBrand().getName() : null)
            .adminId(item.getAdmin() != null ? item.getAdmin().getId() : null)
            .createdDate(item.getCreatedDate().toString())
            .build();
    }

    // 혹시 여러 개를 한 번에 변환할 때 편리한 정적 메서드
    public static List<ItemResponseDTO> toDTOList(List<Item> items) {
        return items.stream().map(ItemResponseDTO::toDTO).collect(Collectors.toList());
    }
}