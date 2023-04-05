package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.sale.Sale;
import com.example.weblogin.domain.saleitem.SaleItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleInfo {
    private Long id;
    private LocalDateTime orderDate;
    private int totalPrice;
    private List<SaleItem> saleItemList;

    public SaleInfo(Sale sale) {
        this.id = sale.getId();
        this.orderDate = sale.getSaleDate();
        this.totalPrice = sale.getTotalProfit();
        this.saleItemList = sale.getSaleItems();
    }

    public SaleInfo(LocalDateTime saleDate, int totalProfit) {
        this.orderDate = saleDate;
        this.totalPrice = totalProfit;
    }
}