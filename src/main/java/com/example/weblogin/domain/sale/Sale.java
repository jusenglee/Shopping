package com.example.weblogin.domain.sale;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.weblogin.config.baseEntity.BaseEntity;
import com.example.weblogin.domain.saleitem.SaleItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Entity(name = "sale")

public class Sale extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sale_id")
	private Long id;

	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
	private List<SaleItem> saleItems = new ArrayList<>();

	@Column(nullable = false)
	private LocalDateTime saleDate;

	@Embedded
	private Integer totalProfit;

	public Sale() {
	}

	public Sale(LocalDateTime saleDate, Integer totalProfit) {
		this.saleDate = saleDate;
		this.totalProfit = totalProfit;
	}

	public Long getId() {
		return id;
	}

	public List<SaleItem> getSaleItems() {
		return saleItems;
	}

	public LocalDateTime getSaleDate() {
		return saleDate;
	}

	public Integer getTotalProfit() {
		return totalProfit;
	}

	public void addSaleItem(SaleItem saleItem) {
		saleItems.add(saleItem);
		saleItem.setSale(this);
	}
	// getter, setter, equals, hashCode 등 생략
}
