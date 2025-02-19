package com.example.weblogin.domain.item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.jetbrains.annotations.NotNull;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemOption.ItemOption;
import com.example.weblogin.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Item")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "item_id")
	private Long id; //상품 코드

	@Column(nullable = false, length = 50)
	private String itemNm; //상품명

	@Column(name = "price", nullable = false)
	private Integer price;  //가격

	@Column(name = "sale_per", nullable = false)
	private Integer salePer;  //할인률

	@Lob
	@Column(nullable = false)
	private String itemDetail;  //상품 상세설명

	@Enumerated(EnumType.STRING)
	private ItemSellStatus itemSellStatus;  //상품 판매 상태

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ItemOption> options;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Categorie category; // 카테고리 번호

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Brand brand;    //브랜드 번호

	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer heart = 0; // 좋아요 수

	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer countview = 0; //조회수

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)

	private List<ItemImg> itemImgs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member admin;    //상품 게시자

	public static Item toEntity(@NotNull ItemCreateRequest request, Categorie category, Brand brand, Member mber) {
		return Item.builder()
			.itemNm(request.getItemNm())
			.price(request.getPrice())
			.admin(mber)
			.salePer(request.getSalePer() != null ? request.getSalePer() : 0)
			.itemDetail(request.getItemDetail())
			.itemSellStatus(request.getItemSellStatus())
			.countview(0)
			.heart(0)
			.category(category)
			.brand(brand)
			.itemImgs(new ArrayList<>())
			.options(new ArrayList<>())
			.build();
	}

	public void addOption(ItemOption option) {
		this.options.add(option);
		option.setItem(this);  // 양방향 연관관계 설정
	}

	public void addItemImg(ItemImg itemImg) {
		this.itemImgs.add(itemImg);
		itemImg.setItem(this); // 연관관계 주인 쪽에 this(Item) 할당
	}

	/**
	 * 상품정보 업데이트
	 * @param itemFormDto 전달받은 상품 정보
	 */
	public void updateItem(@NotNull ItemCreateRequest itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.salePer = itemFormDto.getSalePer();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
	}

	public void updateSellStatus(ItemSellStatus sellStatus) {
		this.itemSellStatus = sellStatus;
	}

}
