package com.example.weblogin.domain.saleitem;

import com.example.weblogin.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    List<SaleItem> findAll();

    List<SaleItem> findByItem(Long itemId);

    List<SaleItem> findAllByItem(Item item);
}
