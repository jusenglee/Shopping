package com.example.weblogin.domain.item;


import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import com.example.weblogin.domain.itemCategory.QKategorie;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<MainItemDto> search(ItemSearchRequestDTO request) {
        QItem item = QItem.item;
        QKategorie kategorie = new QKategorie("kategorie");

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getName() != null && !request.getName().isEmpty()) {
            builder.and(item.itemNm.likeIgnoreCase("%" + request.getName() + "%"));
        }

        if (request.getBrandIds() != null && !request.getBrandIds().isEmpty()) {
            builder.and(item.brand.id.in(request.getBrandIds()));
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Long> categoryIds = request.getCategoryIds() != null ? request.getCategoryIds() : Collections.emptyList();
            BooleanExpression categoryCondition = kategorie.id.in(categoryIds.toArray(new Long[0]));
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
        query.select(item)
                .from(item)
                .join(item.category, kategorie)
                .where(builder)
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<MainItemDto> mainItemDto = query.fetch();
        long totalCount = query.fetchCount();
        return new PageImpl<>(mainItemDto, pageable, totalCount);
    }
}
