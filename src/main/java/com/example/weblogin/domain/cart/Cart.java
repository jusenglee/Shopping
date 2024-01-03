package com.example.weblogin.domain.cart;

import com.example.weblogin.config.BaseEntity;
import com.example.weblogin.domain.cartItem.CartItem;
import com.example.weblogin.domain.delivery.Delivery;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.order.Order;
import com.example.weblogin.domain.orderItem.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Cart")
@Getter
@Setter
public class Cart extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart() {
        // for JPA
    }

    // 생성 메소드
    public Cart(Member member) {
        this.member = member;
    }

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.member = member;
        return cart;
    }

    // 카트에 새로운 상품을 추가
    public void addItem(Item item, Integer quantity) {
        CartItem cartItem = findCartItem(item.getId());

        if (cartItem != null) {
            cartItem.changeQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(this, item, quantity);
            cartItems.add(cartItem);
        }

        item.decreaseStockQuantity(quantity);
    }

    //카트에서 모든 상품을 삭제
    public void clearItems() {
        for (CartItem cartItem : cartItems) {
            cartItem.cancel();
        }
        cartItems.clear();
    }
    //카트에 담긴 모든 상품을 주문하는 메서드
    //카트 아이템을 주문 아이템으로 변환한 뒤, 이를 새로운 주문 인스턴스에 추가하여 저장. 마지막으로 카트에서 모든 상품을 삭제
    public void order(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = Order.createOrder(member, delivery, orderItems);
        cartItems.forEach(cartItem -> order.addOrderItem(cartItem.toOrderItem()));
        clearItems();
    }

    //카트에 담긴 모든 상품을 조회
    public List<CartItem> getCartItems() {
        return Collections.unmodifiableList(cartItems);
    }

    //카트에 담긴 모든 상품의 총 가격을 계산
    public Integer getTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    //상품 정보 찾기
    private CartItem findCartItem(Long itemId) {
        return cartItems.stream()
                .filter(cartItem -> cartItem.isSameItem(itemId))
                .findFirst()
                .orElse(null);
    }

    //카트에 상품이 하나도 없는지 여부를 반환
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
    //주어진 카트 아이템을 카트에서 삭제
    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }
}