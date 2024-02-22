package com.example.weblogin.domain.DTO;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.member.Member;

import lombok.Data;
import lombok.NoArgsConstructor;

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
	private Member admin;//브랜드 계정
	@NotNull(message = "상품 상태는 필수 입력 값입니다.")
	private ItemSellStatus itemSellStatus;
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
	private List<Long> itemImgIds = new ArrayList<>();
	private List<MultipartFile> itemImgFile = new ArrayList<>();
	private Integer countview;
	private Integer heart;

	public static ItemFormDto of(Item entity) {
		ItemFormDto itemFormDto = new ItemFormDto();
		itemFormDto.setId(entity.getId());
		itemFormDto.setCategory(entity.getCategory().getId());
		itemFormDto.setBrand(entity.getBrand().getId());
		itemFormDto.setAdmin(entity.getAdmin());
		itemFormDto.setItemNm(entity.getItemNm());
		itemFormDto.setItemDetail(entity.getItemDetail());
		itemFormDto.setItemSellStatus(entity.getItemSellStatus());
		itemFormDto.setPrice(entity.getPrice());
		itemFormDto.setStock(entity.getStockNumber());
		itemFormDto.setCountview(entity.getCountview());
		itemFormDto.setHeart(entity.getHeart());
		return itemFormDto;
	}

	public Integer getCountview() {
		return countview;
	}

	public Integer getHeart() {
		return heart;
	}

	public Item toEntity(Brand brand, Categorie category) {
		Item item = new Item();
		item.setItemNm(itemNm);
		item.setItemDetail(itemDetail);
		item.setItemSellStatus(itemSellStatus);
		item.setPrice(price);
		item.setStockNumber(stock);
		item.setCategory(category);
		item.setBrand(brand);
		item.setCountview(0);
		item.setHeart(0);
		item.setAdmin(admin);
		return item;
	}
}
