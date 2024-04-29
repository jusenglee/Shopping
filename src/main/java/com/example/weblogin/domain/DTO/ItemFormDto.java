package com.example.weblogin.domain.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.member.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemFormDto {

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
	@NotNull(message = "재고는 필수 입력 값입니다. ")
	private Map<String, Integer> inventory = new HashMap<>();
	private Member admin;
	@NotNull(message = "상품 상태는 필수 입력 값입니다. ")
	private ItemSellStatus itemSellStatus;
	@NotNull(message = "상품 이미지는 필수 입력 값입니다. ")
	private List<ItemImg> itemImgDtoList;
	private Integer countview;
	private Integer heart;

	@Builder
	public ItemFormDto(Long id, Long category, Long brand, String itemNm, Integer price, String itemDetail,
		Map<String, Integer> inventory, Member admin, ItemSellStatus itemSellStatus, List<ItemImg> itemImgDtoList,
		Integer countview, Integer heart) {
		this.id = id;
		this.category = category;
		this.brand = brand;
		this.itemNm = itemNm;
		this.price = price;
		this.itemDetail = itemDetail;
		this.inventory = inventory;
		this.admin = admin;
		this.itemSellStatus = itemSellStatus;
		this.itemImgDtoList = itemImgDtoList;
		this.countview = countview;
		this.heart = heart;
	}
}
