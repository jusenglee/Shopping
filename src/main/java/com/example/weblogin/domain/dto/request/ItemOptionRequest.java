package com.example.weblogin.domain.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.example.weblogin.domain.itemOption.ItemOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOptionRequest {

	@NotBlank(message = "색상 필수 입력 값입니다. ")
	@Builder.Default
	private String color = null;
	@NotBlank(message = "사이즈 필수 입력 값입니다. ")
	@Builder.Default
	private String size = null;
	@NotBlank(message = "소재 필수 입력 값입니다. ")
	@Builder.Default
	private String material = null;
	@Min(value = 1, message = "재고 필수 입력 값입니다. ")
	@Builder.Default
	private Integer stock = null;

	public static ItemOptionRequest toDTO(ItemOption entity) {
	        return ItemOptionRequest.builder()
	                .color(entity.getColor().name())
	                .size(entity.getSize().name())
	                .material(entity.getMaterial().name())
	                .stock(entity.getStock())
	                .build();
	    }
	public static List<ItemOptionRequest> toDTOList(List<ItemOption> options) {
		List<ItemOptionRequest> result = new ArrayList<>();
		for(ItemOption val : options){
			result.add(toDTO(val));
		}
		return result;
	}

}
