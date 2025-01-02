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

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.DTO.ItemOptionRequest;
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

	@Builder.Default
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ItemOption> options = new ArrayList<>();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Categorie category; // 카테고리 번호

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "brand_id")
	private Brand brand;    //브랜드 번호

	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer heart = 0; // 좋아요 수

	@Column(columnDefinition = "integer default 0", nullable = false)
	private Integer countview = 0; //조회수

	@OneToMany(mappedBy = "item")
	@JsonManagedReference
	private List<ItemImg> itemImgs; // 상품 이미지

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")

	private Member admin;    //상품 게시자

	public static Item toEntity(ItemCreateRequest request,Categorie category ,Brand brand ) {
        return Item.builder()
                .itemNm(request.getItemNm())
                .price(request.getPrice())
                .admin(request.getAdmin())
				.salePer(request.getSalePer() != null ? request.getSalePer() : 0)
                .itemDetail(request.getItemDetail())
                .itemSellStatus(request.getItemSellStatus())
                .countview(0)
                .heart(0)
                .category(category)
                .brand(brand)
                .build();
    }

	public void addOptionList(List<ItemOptionRequest> options) {
	    // this.options가 null이면 예외 발생 → 반드시 1)에서 초기화되어 있어야 함
	    for (ItemOptionRequest req : options) {
	        ItemOption option = ItemOption.toEntity(req);
	        option.setItem(this);  // 양방향 연관관계 설정
	        this.options.add(option);
	    }
	}

	/**
	 * 상품정보 업데이트
	 * @param itemFormDto 전달받은 상품 정보
	 */
	public void updateItem(ItemCreateRequest itemFormDto) {
		this.itemNm = itemFormDto.getItemNm();
		this.price = itemFormDto.getPrice();
		this.itemDetail = itemFormDto.getItemDetail();
		this.itemSellStatus = itemFormDto.getItemSellStatus();
		addOptionList(itemFormDto.getOptions());
	}
}
