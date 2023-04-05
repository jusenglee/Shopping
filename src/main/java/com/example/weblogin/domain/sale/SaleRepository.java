package com.example.weblogin.domain.sale;

import com.example.weblogin.domain.DTO.SaleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAll();

    List<Sale> findAllByOrderByIdDesc();
}
