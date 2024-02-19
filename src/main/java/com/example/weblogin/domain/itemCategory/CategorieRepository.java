package com.example.weblogin.domain.itemCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
	// ID를 기반으로 Kategorie 엔티티를 찾는 메소드
	Categorie findKategorieById(Long id);
}
