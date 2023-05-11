package com.example.weblogin.domain.item;

import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

    Page<MainItemDto> search(ItemSearchRequestDTO request);

}
