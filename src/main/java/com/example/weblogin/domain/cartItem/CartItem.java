package com.example.weblogin.domain.cartItem;

import static javax.persistence.FetchType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.cart.Cart;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.orderItem.OrderItem;

import lombok.Getter;

@Entity(name = "CartItem")
@Getter
public class CartItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "quantity")
	private Integer quantity;

	protected CartItem() {
		// for JPA
	}

	public CartItem(Cart cart, Item item, Integer quantity) {
		this.cart = cart;
		this.item = item;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Item getItem() {
		return item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void changeQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getTotalPrice() {
		return getItem().getPrice() * getQuantity();
	}

	public void cancel() {
		getItem().addStockQuantity(quantity);
	}

	public OrderItem toOrderItem() {
		return OrderItem.createOrderItem(item, item.getPrice(), quantity);
	}

	public boolean isSameItem(Long item) {
		return this.item.getId().equals(item);
	}
}
