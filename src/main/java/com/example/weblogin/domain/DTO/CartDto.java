package com.example.weblogin.domain.DTO;


import com.example.weblogin.domain.cart.Cart;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {
    private Long id;
    private List<CartItemDto> cartItems;

    private int totalQuantity;

    private int totalPrice;

    public CartDto(Cart cart) {
        this.id = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());
        this.totalQuantity = cartItems.stream().mapToInt(CartItemDto::getQuantity).sum();
        this.totalPrice = cart.getTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
