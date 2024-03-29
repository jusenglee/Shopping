package com.example.weblogin.domain.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import com.example.weblogin.domain.DTO.QMainItemDto;
import com.example.weblogin.domain.ItemImg.QItemImg;
import com.example.weblogin.domain.itemCategory.QBrand;
import com.example.weblogin.domain.itemCategory.QCategorie;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<MainItemDto> search(ItemSearchRequestDTO request) {
		QItem item = QItem.item;
		QCategorie categorie = QCategorie.categorie;
		QItemImg itemImg = QItemImg.itemImg;
		QBrand brand = QBrand.brand;

		BooleanBuilder builder = new BooleanBuilder();

		if (request.getName() != null && !request.getName().isEmpty()) {
			builder.and(item.itemNm.likeIgnoreCase("%" + request.getName() + "%"));
		}

		if (request.getBrandIds() != null && !request.getBrandIds().isEmpty()) {
			builder.and(item.brand.id.in(request.getBrandIds()));
		}
		if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
			List<Long> categoryIds =
				request.getCategoryIds() != null ? request.getCategoryIds() : Collections.emptyList();
			BooleanExpression categoryCondition = categorie.id.in(categoryIds.toArray(new Long[0]));
			builder.and(categoryCondition);
		}

		List<OrderSpecifier<?>> orders = new ArrayList<>();
		if (request.getSortBy() != null) {
			switch (request.getSortBy()) {
				case PRICE_ASC:
					orders.add(item.price.asc());
					break;
				case PRICE_DESC:
					orders.add(item.price.desc());
					break;
				case POPULARITY:
					orders.add(item.countview.desc());
					break;
				default:
					break;
			}
		}

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
		JPAQuery<MainItemDto> query = new JPAQuery<>(entityManager);
		query.select(new QMainItemDto(
				item.id,
				item.itemNm,
				item.itemDetail,
				itemImg.imgUrl,
				item.price,
				brand.name,
				categorie.cateName,
				item.countview,
				item.heart
			))
			.from(item)
			.join(item.category, categorie)
			.join(item.brand, brand)
			.join(item.itemImgs, itemImg) // fetchJoin() is used here
			.where(builder, itemImg.repimgYn.eq("Y"))
			.orderBy(orders.toArray(new OrderSpecifier[0]))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		List<MainItemDto> mainItemDto = query.fetch();
		long totalCount = query.fetchCount();
		return new PageImpl<>(mainItemDto, pageable, totalCount);
	}
}
