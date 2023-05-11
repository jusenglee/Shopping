package com.example.weblogin.domain.DTO;

import com.example.weblogin.domain.itemCategory.Kategorie;
import lombok.Data;

@Data
public class KategorieDto {
    private Long id;
    private String kateName;

    public KategorieDto(Kategorie kategorie) {
        this.id = kategorie.getId();
        this.kateName = kategorie.getName();
    }


}