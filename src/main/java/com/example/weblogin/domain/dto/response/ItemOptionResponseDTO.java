package com.example.weblogin.domain.dto.response;

import com.example.weblogin.domain.itemOption.ItemOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOptionResponseDTO {

	private Long id;
	private String colorType;
	private String sizeType;
	private String materialType;
	private Integer stock;
	// etc...

	public static ItemOptionResponseDTO toDTO(ItemOption entity) {
		if (entity == null) {
			return null;
		}
		return ItemOptionResponseDTO.builder()
			.id(entity.getId())
			.colorType(entity.getColor().name())
			.sizeType(entity.getSize().name())
			.materialType(entity.getMaterial().name())
			.stock(entity.getStock())
			.build();
	}
}