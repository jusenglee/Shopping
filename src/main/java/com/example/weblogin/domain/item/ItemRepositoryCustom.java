package com.example.weblogin.domain.item;

import com.example.weblogin.domain.DTO.ItemResponse;
import com.example.weblogin.domain.DTO.ItemSearchRequest;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

    Page<ItemResponse> search(ItemSearchRequest request);

}
