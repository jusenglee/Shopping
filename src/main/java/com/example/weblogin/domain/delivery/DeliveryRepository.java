package com.example.weblogin.domain.delivery;

import com.example.weblogin.domain.item.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Item, Long> {
}
