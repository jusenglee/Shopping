package com.example.weblogin.domain.itemCategory;

import com.example.weblogin.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "kategorie")
public class Kategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kateName;

    private String kateCodeRef;

    Kategorie(String name) {
        this.kateName = name;
    }

    public String getName() {
        return kateName;
    }



}