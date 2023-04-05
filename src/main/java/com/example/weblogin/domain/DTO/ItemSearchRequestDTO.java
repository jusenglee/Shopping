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

    public List<Long> getCategoryIds() {
        return categoryIds;
    }
}

