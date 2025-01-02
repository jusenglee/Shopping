package com.example.weblogin.domain.DTO;

import java.util.ArrayList;
import java.util.List;

import com.example.weblogin.domain.itemOption.ColorType;
import com.example.weblogin.domain.itemOption.ItemOption;
import com.example.weblogin.domain.itemOption.MaterialType;
import com.example.weblogin.domain.itemOption.SizeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOptionRequest {

	private ColorType color;

	private SizeType size;

	private MaterialType material;

	private Integer stock;

	public static ItemOptionRequest toDTO(ItemOption entity) {
	        return ItemOptionRequest.builder()
	                .color(entity.getColor())
	                .size(entity.getSize())
	                .material(entity.getMaterial())
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
