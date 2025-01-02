package com.example.weblogin.domain.DTO;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest  {

	private Long id;

	@NotNull(message = "카테고리 필수 입력 값입니다. ")
	private Long category;

	@NotNull(message = "브랜드 필수 입력 값입니다. ")
	private Long brand;

	@NotBlank(message = "상품명은 필수 입력 값입니다. ")
	private String itemNm;

	@NotNull(message = "가격은 필수 입력 값입니다.")
	private Integer price;

	@NotBlank(message = "상세 내용은 필수 입력 값입니다.")
	private String itemDetail;

	private List<ItemOptionRequest> options;

	@NotNull(message = "상품 상태는 필수 입력 값입니다. ")
	private ItemSellStatus itemSellStatus;

	@NotNull(message = "상품 이미지는 필수 입력 값입니다. ")
	private List<ItemImg> itemImgDtoList;

	private Integer countview = 0;

	private Integer heart = 0;

	private Integer salePer = 0;

	private Member admin;

	public static ItemCreateRequest toDTO(Item entity) {
        return ItemCreateRequest.builder()
                .id(entity.getId())
                .category(entity.getCategory().getId())
                .brand(entity.getBrand().getId())
                .itemNm(entity.getItemNm())
                .itemDetail(entity.getItemDetail())
                .price(entity.getPrice())
				.salePer(entity.getSalePer())
                .itemSellStatus(entity.getItemSellStatus())
                .admin(entity.getAdmin())
                .options(ItemOptionRequest.toDTOList(entity.getOptions()))
                .build();
    }
}
