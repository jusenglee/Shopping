package com.example.weblogin.domain.itemOption;

import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.weblogin.domain.dto.request.ItemOptionRequest;
import com.example.weblogin.domain.item.Item;

import com.example.weblogin.service.Utils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "item_option")
@AllArgsConstructor
@Getter
@Builder
public class ItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 Item에 속하는 옵션인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private Item item;

    // 색상, 사이즈, 재질을 Enum으로
    @Enumerated(EnumType.STRING)
    private ColorType color;

    @Enumerated(EnumType.STRING)
    private SizeType size;

    @Enumerated(EnumType.STRING)
    private MaterialType material;

    // 재고수량
    private Integer stock;

    public ItemOption() {

    }

    public static ItemOption toEntity(ItemOptionRequest request) {
        Optional<ColorType> color = Utils.convertToEnum(request.getColor(), ColorType.class);
        Optional<SizeType> size = Utils.convertToEnum(request.getSize(), SizeType.class);
        Optional<MaterialType> material = Utils.convertToEnum(request.getMaterial(), MaterialType.class);

        return ItemOption.builder().color(color.get()).size(size.get()).material(material.get()).stock(request.getStock()).build();
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
