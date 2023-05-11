package com.example.weblogin.domain.DTO;

import lombok.Data;

import java.util.List;
@Data
public class ItemSearchRequestDTO {
    private String name;
    private List<Long> brandIds;

    private List<Long> categoryIds;
    private SortBy sortBy;
    private int page;
    private int size;

    public enum SortBy {
        PRICE_ASC,
        PRICE_DESC,
        POPULARITY
    }
    public ItemSearchRequestDTO(String name, List<Long> brandIds, List<Long> categoryIds, SortBy sortBy, int page, int size) {
        this.name = name;
        this.brandIds = brandIds;
        this.categoryIds = categoryIds;
        this.sortBy = sortBy;
        this.page = page;
        this.size = size;
    }
}

