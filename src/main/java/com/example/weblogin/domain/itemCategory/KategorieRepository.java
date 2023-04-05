package com.example.weblogin.domain.itemCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategorieRepository extends JpaRepository<Kategorie,Long> {
}
