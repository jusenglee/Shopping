package com.example.weblogin.domain.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.example.weblogin.domain.dto.FileDTO;
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
	private List<FileDTO> itemImgList;

	// 옵션 목록
	private List<ItemOptionResponseDTO> itemOptionList;

	private String cateName;

	private String brandName;

	private String createdDate;

	public static ItemResponseDTO toDTO(Item item) {
		return ItemResponseDTO.builder()
			.id(item.getId())
			.itemNm(item.getItemNm())
			.price(item.getPrice())
			.salePer(item.getSalePer())
			.itemDetail(item.getItemDetail())
			.itemSellStatus(item.getItemSellStatus() != null ? item.getItemSellStatus().name() : null)
			.itemImgList(
				item.getItemImages().stream()
					.map(FileDTO::toDTO)
					.collect(Collectors.toList())
			)
			.itemOptionList(
				item.getOptions().stream()
					.map(ItemOptionResponseDTO::toDTO)
					.collect(Collectors.toList())
			)
			.cateName(item.getCategory() != null ? item.getCategory().getCateName() : null)
			.brandName(item.getBrand() != null ? item.getBrand().getName() : null)
			.build();
	}
}