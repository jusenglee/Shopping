package com.example.weblogin.controller.RestController;


import com.example.weblogin.domain.DTO.OrderHistoryDto;
import com.example.weblogin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

public class RestOrderController {

    private final OrderService orderService;

    //주문내역 정보 보내기
    @PostMapping("/user/orderHist/{page}")
    public Map<String,Object> orderList(@PathVariable("page") Optional<Integer> page, Principal principal) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistoryDto> orderHistoryDtoList = orderService.findOrderHistory(principal.getName(), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("orders", orderHistoryDtoList);
        map.put("page", pageable.getPageNumber());
        map.put("maxPage", 5);

        return map;
    }

    // 장바구니 상품 전체 주문
    @Transactional
    @PostMapping("/user/cart/checkout/{id}")
    public void cartCheckout(@PathVariable("id") Long id) {
        orderService.placeOrderFromCart(id);
    }

    // 상품 개별 주문 -> 상품 상세페이지에서 구매하기 버튼으로 주문
    @Transactional
    @PostMapping("/user/{id}/checkout/{itemId}")
    public void checkout(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, int count) {
        // 로그인이 되어있는 유저의 id와 주문하는 id가 같아야 함
        orderService.placeOrder(id, itemId, count);

    }
}
