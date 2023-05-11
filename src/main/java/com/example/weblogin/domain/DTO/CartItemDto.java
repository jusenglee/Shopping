package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.cartItem.CartItem;
import lombok.Data;

@Data
public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private String itemName;
    private int price;
    private int quantity;

    public CartItemDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId();
        this.itemId = cartItem.getItem().getId();
        this.itemName = cartItem.getItem().getItemNm();
        this.price = cartItem.getItem().getPrice();
        this.quantity = cartItem.getQuantity();
    }

}