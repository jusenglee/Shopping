package com.example.weblogin.service;

import com.example.weblogin.domain.itemCategory.Kategorie;
import com.example.weblogin.domain.itemCategory.KategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KategorieService {
    private final KategorieRepository kategorieRepository;

    @Autowired
    public KategorieService(KategorieRepository kategorieRepository) {
        this.kategorieRepository = kategorieRepository;
    }

    public Kategorie findKategorieById(Long id) {
        return kategorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. id: " + id));
    }
}
