package com.example.weblogin.domain.saleitem;


import com.example.weblogin.config.BaseEntity;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.sale.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Builder

@AllArgsConstructor
@Getter
@Entity(name = "saleItem")

public class SaleItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_item_id")
    private Long Item_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
    private Integer salePrice;
    private Integer count;

    public SaleItem() {
    }

    public SaleItem(Sale sale, Item item, Integer salePrice, Integer count) {
        this.sale = sale;
        this.item = item;
        this.salePrice = salePrice;
        this.count = count;
    }

    //주문 만들기
    public static SaleItem createSaleItem(Sale sale, Item item, Integer orderPrice, Integer count) {
        SaleItem saleItem = new SaleItem();
        saleItem.setSale(sale);
        saleItem.setItem(item);
        saleItem.setSalePrice(orderPrice);
        saleItem.setCount(count);
        return saleItem;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //수익
    public Integer getProfit() {
        return getSalePrice() - getItem().getPrice();
    }
}

