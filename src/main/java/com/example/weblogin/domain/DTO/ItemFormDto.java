package com.example.weblogin.domain.DTO;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.member.Member;

import lombok.Builder;
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
	private Integer stockNumber;
	private Member admin;
	@NotNull(message = "상품 상태는 필수 입력 값입니다.")
	private ItemSellStatus itemSellStatus;
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();// 이미지를 클라이언트로
	private List<MultipartFile> itemImgFile = new ArrayList<>(); // 이미지를 서버로
	private List<String> deleteImgIds = new ArrayList<>(); //삭제 대상 이미지 Id
	private Integer countview;
	private Integer heart;

	@Builder
	public ItemFormDto(Long id, Long category, Long brand, String itemNm, Integer price, String itemDetail,
		Integer stockNumber, Member admin, ItemSellStatus itemSellStatus, List<ItemImgDto> itemImgDtoList,
		Integer countview, Integer heart) {
		this.id = id;
		this.category = category;
		this.brand = brand;
		this.itemNm = itemNm;
		this.price = price;
		this.itemDetail = itemDetail;
		this.stockNumber = stockNumber;
		this.admin = admin;
		this.itemSellStatus = itemSellStatus;
		this.itemImgDtoList = itemImgDtoList;
		this.countview = countview;
		this.heart = heart;
	}
}
