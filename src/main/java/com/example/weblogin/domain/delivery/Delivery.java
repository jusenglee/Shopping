package com.example.weblogin.domain.delivery;


import com.example.weblogin.domain.order.Order;
import lombok.Data;

import javax.persistence.*;
@Entity(name = "Delivery")
@Data
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private String address;

    @Enumerated(EnumType.STRING)

    private DeliveryStatus deliveryStatus;

    public Delivery() {}

    public Delivery(String address) {
        this.address = address;
        this.deliveryStatus = DeliveryStatus.Ready;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static Delivery createDelivery(String address) {
        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setDeliveryStatus(DeliveryStatus.Ready);
        return delivery;
    }

    public void completeDelivery() {
        this.deliveryStatus = DeliveryStatus.Done;
    }
}