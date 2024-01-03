package com.example.weblogin.domain.itemCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    // ID를 기반으로 Brand 엔티티를 찾는 메소드
    Brand findBrandById(Long id);
}
