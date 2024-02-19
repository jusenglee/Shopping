package com.example.weblogin.domain.item;

import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.weblogin.domain.member.Member;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByitemSellStatus(ItemSellStatus saleStatus);
    List<Item> findByAdmin(Member admin);
}
