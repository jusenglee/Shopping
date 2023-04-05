package com.example.weblogin.controller.RestController;

import com.example.weblogin.domain.DTO.CartDto;
import com.example.weblogin.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestCartController {
    private final CartService cartService;

    //장바구니 정보 불러오기
    @PostMapping("/user/cart/{id}")
    public CartDto userCartPage(@PathVariable("id") Long id) {
        return cartService.getCart(id);
    }

    //회원 장바구니에 물건 넣기
    @PostMapping("/user/cart/{id}/{itemId}")
    public void addCartItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, int amount) {
        cartService.addItemToCart(id, itemId, amount);
    }

    //장바구니 물건 삭제
    @PostMapping("/user/cart/{id}/{cartItemId}/delete")
    public void removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
    }


}
