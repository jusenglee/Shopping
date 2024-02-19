package com.example.weblogin.domain.orderItem;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.order.Order;

import lombok.Getter;

@Entity
@Getter
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "order_item_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private Integer orderPrice; // 주문 가격
	private Integer count; // 주문 수량

	protected OrderItem() {
	}

	//주문 생성
	public static OrderItem createOrderItem(Item item, Integer orderPrice, Integer count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);

		item.removeStockQuantity(count);
		return orderItem;
	}

	//==비즈니스 로직==//
	public void cancel() {
		getItem().addStockQuantity(count);
	}

	public Integer getTotalPrice() {
		return getOrderPrice() * getCount();
	}

	//==Getter, Setter==//
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
