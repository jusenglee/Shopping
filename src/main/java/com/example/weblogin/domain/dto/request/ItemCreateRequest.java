package com.example.weblogin.domain.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.weblogin.domain.dto.FileDTO;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemSellStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 상품 저장을 위해 클라이언트로부터 데이터를 받아오는 DTO
 */
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

	@Min(value = 1, message = "삼품의 가격을 입력해주세요.")
	private Integer price;

	@NotBlank(message = "상세 내용은 필수 입력 값입니다.")
	private String itemDetail;

	@Valid
	private List<ItemOptionRequest> options;

	private ItemSellStatus itemSellStatus;

	private List<FileDTO> itemImgDtoList;

	private Integer countview = 0;

	private Integer heart = 0;

	private Integer salePer = 0;

	private Long sellerId;

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
                .sellerId(entity.getSeller().getId())
                .options(ItemOptionRequest.toDTOList(entity.getOptions()))
                .itemImgDtoList(entity.getItemImages().stream().map(FileDTO::toDTO).collect(Collectors.toList()))
                .build();
    }
}
