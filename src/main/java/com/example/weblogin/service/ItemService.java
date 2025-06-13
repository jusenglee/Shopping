package com.example.weblogin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.dto.response.ItemResponseDTO;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.CategorieRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final FileService fileService;
    private final CategorieRepository categorieRepository;
    private final BrandRepository brandRepository;

    // 상품정보 가져오기
    @Transactional(readOnly = true)
    public ItemResponseDTO getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        return ItemResponseDTO.toDTO(item);

    }
}
