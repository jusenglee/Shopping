package com.example.weblogin.service;

import com.example.weblogin.config.Exception.ServiceUtils;
import com.example.weblogin.domain.DTO.CartDto;
import com.example.weblogin.domain.DTO.CartItemDto;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.cart.CartRepository;
import com.example.weblogin.domain.cartItem.CartItem;
import com.example.weblogin.domain.cartItem.CartItemRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private  final OrderRepository orderRepository;
    private final SaleRepository saleRepository;


    //카트에 상품 추가
    public void addItemToCart(Long memberId, Long itemId, int quantity) {
        Member member = ServiceUtils.getOrThrow(memberRepository.findById(memberId), "유저를 찾을 수 없습니다.");
        Item item = ServiceUtils.getOrThrow(itemRepository.findById(itemId), "상품을 찾을 수 없습니다.");

        Cart cart = member.getCart();
        //회원애게 카트가 존재하지 않을 경우 카트 새로 생성
        if (cart == null) {
            cart = Cart.createCart(member);
            member.setCart(cart);
            cartRepository.save(cart);
        }
        //장바구니에 상품 추가
        cart.addItem(item, quantity);
    }

    //카트 상품 모두 주문
    public void orderAllCartItems(Long memberId) {
        // 회원과 장바구니 조회
        Member member = ServiceUtils.getOrThrow(memberRepository.findById(memberId), "사용자를 찾을 수 없습니다");
        Cart cart = ServiceUtils.getOrThrow(cartRepository.findByMember(member), "카트를 찾을 수 없습니다");

        if (cart.isEmpty()) {
            throw new IllegalStateException("카트가 비었습니다");
        }
        //배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.Ready);


        //주문 항목 생성
        List<CartItem> cartItems = cart.getCartItems();
        OrderItem[] orderItems = cartItems.stream()
                .map(CartItem::toOrderItem)
                .toArray(OrderItem[]::new);

        //주문 저장
        Order order = Order.of(member, delivery, Arrays.asList(orderItems));
        orderRepository.save(order);
        //판매내역 생성
        Sale sale = new Sale(order.getOrderDate(), order.getTotalPrice());
        //주문상품을 판매내역에 저장
        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            SaleItem saleItem = SaleItem.createSaleItem(sale, item, cartItem.getTotalPrice(), cartItem.getQuantity());
            sale.addSaleItem(saleItem);
        }
        saleRepository.save(sale);
        //카트 상품 지우기
        cart.clearItems();
    }

    //카트 정보 불러오기
    public CartDto getCart(Long memberId) {
        Member member = ServiceUtils.getOrThrow(memberRepository.findById(memberId), "존재하지 않는 회원입니다.");
        Cart cart = ServiceUtils.getOrThrow(cartRepository.findByMember(member), "장바구니가 존재하지 않습니다.");

        List<CartItemDto> cartItemDtoList = cart.getCartItems().stream()
                .map(CartItemDto::new)
                .collect(Collectors.toList());

        return new CartDto(cart);
    }

    //카트 아이템 삭제
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = ServiceUtils.getOrThrow(cartItemRepository.findById(cartItemId),"존재하지 않는 상품입니다.");

        Cart cart = cartItem.getCart();
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }
}
