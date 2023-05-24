package com.example.weblogin.service;

import com.example.weblogin.domain.DTO.OrderHistoryDto;
import com.example.weblogin.domain.DTO.OrderItemDto;
import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.ItemImg.ItemImgRepository;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cart.CartRepository;
import com.example.weblogin.domain.cartItem.CartItem;
import com.example.weblogin.domain.delivery.Delivery;
import com.example.weblogin.domain.delivery.DeliveryStatus;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRepository;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.order.OrderRepository;
import com.example.weblogin.domain.orderItem.OrderItem;
import com.example.weblogin.domain.sale.Sale;
import com.example.weblogin.domain.sale.SaleRepository;
import com.example.weblogin.domain.saleitem.SaleItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final SaleRepository saleRepository;
    private  final ItemImgRepository itemImgRepository;

    /**
     * 상품 주문
     */
    public Long placeOrder(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.Ready);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        Sale sale = new Sale(order.getOrderDate(), order.getTotalPrice());
        saleRepository.save(sale);

        SaleItem saleItem = SaleItem.createSaleItem(sale, item, orderItem.getOrderPrice(), orderItem.getCount());
        sale.addSaleItem(saleItem);

        return order.getId();
    }

    /**
     상품 모두 주문하기_주문 정보를 저장하고 주문과 관련된 데이터들을 처리.
     */
    public void placeOrderFromCart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        Cart cart = cartRepository.findByMember(member)
                .orElseThrow(() -> new IllegalStateException("장바구니가 존재하지 않습니다."));

        if (cart.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있습니다.");
        }

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.Ready);


        List<CartItem> cartItems = cart.getCartItems();
        OrderItem[] orderItems = cartItems.stream()
                .map(CartItem::toOrderItem)
                .toArray(OrderItem[]::new);

        Order order = Order.of(member, delivery, Arrays.asList(orderItems));
        orderRepository.save(order);

        Sale sale = new Sale(order.getOrderDate(), order.getTotalPrice());
        saleRepository.save(sale);

        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            SaleItem saleItem = SaleItem.createSaleItem(sale, item, cartItem.getTotalPrice(), cartItem.getQuantity());
            sale.addSaleItem(saleItem);
        }

        cart.clearItems();
    }

    /**
    주문 조회하기
     */

    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> findOrderHistory(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistoryDto> orderHistoryDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistoryDto.addOrderItemDto(orderItemDto);
            }

            orderHistoryDtos.add(orderHistoryDto);
        }

        return new PageImpl<>(orderHistoryDtos, pageable, totalCount);
    }

    //주문 취소하기
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 주문입니다."));

        order.cancel();
    }
}
