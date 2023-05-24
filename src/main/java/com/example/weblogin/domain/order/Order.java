package com.example.weblogin.domain.order;

import com.example.weblogin.config.BaseEntity;
import com.example.weblogin.domain.delivery.Delivery;
import com.example.weblogin.domain.delivery.DeliveryStatus;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.orderItem.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 생성 메서드. 주문을 생성하기 위한 정적 팩토리 메서드입니다. 주문 생성 시 회원 정보, 배송 정보, 주문 상품 정보를 입력
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    // 연관관계 메서드
    public void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //주문 상품 정보를 추가하는 메서드. 이때, 주문 상품과 주문의 양방향 연관관계를 설정
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    //주문을 생성하는 메서드입니다. createOrder()와의 차이점은 OrderItem 리스트를 받는다
    public static Order of(Member member, Delivery delivery, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    // 주문 취소
    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.Done) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 주문상태 변경
    public void changeOrderStatus(OrderStatus orderStatus) {
        if (delivery.getDeliveryStatus() == DeliveryStatus.Done) {
            throw new IllegalStateException("이미 배송완료된 상품의 주문상태는 변경이 불가능합니다.");
        }

        this.setStatus(orderStatus);
    }

    // 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public Integer getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
