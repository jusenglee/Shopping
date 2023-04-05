package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.sale.Sale;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SaleDto {
    private Long id;
    private LocalDateTime orderDate;
    private int totalPrice;
    private List<SaleItemDto> saleItems;

    public SaleDto(Sale sale) {
        this.id = sale.getId();
        this.orderDate = sale.getSaleDate();
        this.totalPrice = sale.getTotalProfit();
        this.saleItems = sale.getSaleItems().stream()
                .map(SaleItemDto::new)
                .collect(Collectors.toList());
    }
}
