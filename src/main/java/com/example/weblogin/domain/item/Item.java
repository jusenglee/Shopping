package com.example.weblogin.domain.item;

import com.example.weblogin.config.BaseEntity;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.Kategorie;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Item")
@Setter
@NoArgsConstructor
@Getter
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "item_id")
    private Long id; //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private Integer price;  //가격

    @Column(nullable = false)
    private Integer stockNumber;  //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Kategorie category; // 카테고리 번호

    private LocalDateTime createdDate; // 생성 날짜

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;    //브랜드 번호

    @Column(columnDefinition = "integer default 0", nullable = false)//조회수 jh
    //본래 @ManyToMany 다대다 관계의 경우 그대로 사용하지 못하고 반드시 정규화를 통해 중간 테이블을 만들어줘야 합니다. dltmdwn00
    private Integer heart; // 조회수

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer countview; //조회수 jh

    @OneToMany(mappedBy = "item")
    private List<ItemImg> itemImgs; // 상품 이미지

    @Builder
    public Item(String itemNm, Integer price, Integer stockNumber,
                String itemDetail, ItemSellStatus itemSellStatus,
                LocalDateTime createdDate, Integer countview, Integer heart, Kategorie category,Brand brand) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber =stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.createdDate = createdDate;
        this.countview = countview;
        this.heart = heart;
        this.category = category;
        this.brand = brand;
    }

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
    public void addStockQuantity(Integer quantity) {
        this.stockNumber += quantity;
    }

    public void removeStockQuantity(Integer quantity) {
        Integer restStockQuantity = this.stockNumber - quantity;
        if (restStockQuantity < 0) {
            throw new IllegalStateException("need more stock");
        }
        this.stockNumber = restStockQuantity;
    }

    public void decreaseStockQuantity(Integer quantity) {
        Integer restStock = this.stockNumber - quantity;
        if (restStock < 0) {
            throw new IllegalStateException("need more stock");
        }
        this.stockNumber = restStock;
    }


    public void stopSelling() {
        if (this.itemSellStatus == ItemSellStatus.STOPPED) {
            throw new IllegalStateException("이미 판매가 중지된 상품입니다.");
        }
        this.itemSellStatus = itemSellStatus.STOPPED;
    }

    /**
     * Resume selling the item
     */
    public void resumeSelling() {
        if (this.itemSellStatus == ItemSellStatus.SELL) {
            throw new IllegalStateException("이미 판매중인 상품입니다.");
        }
        this.itemSellStatus = ItemSellStatus.SELL;
    }
    public void increaseHeart() {
        this.heart += 1;
    }
}
