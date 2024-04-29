package com.example.weblogin.domain.itemInventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.weblogin.domain.item.Item;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Item_Inventory")
@NoArgsConstructor
@Getter
public class ItemInventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item ItemId;

	@Column(nullable = false)
	private String size;

	@Column(nullable = false)
	private Integer stock;
}
