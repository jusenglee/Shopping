package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.saleitem.SaleItem;
import lombok.Data;

@Data
public class SaleItemDto {
    private Long itemId;
    private String itemName;
    private int price;
    private int count;
    private int profit;

    public SaleItemDto(SaleItem saleItem) {
        this.itemId = saleItem.getItem().getId();
        this.itemName = saleItem.getItem().getItemNm();
        this.price = saleItem.getSalePrice();
        this.count = saleItem.getCount();
        this.profit = saleItem.getProfit();
    }
}
