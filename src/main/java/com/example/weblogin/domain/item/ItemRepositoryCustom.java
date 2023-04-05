package com.example.weblogin.domain.item;

import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

    Page<Item> search(ItemSearchRequestDTO request);

}
