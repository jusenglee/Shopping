package com.example.weblogin.service;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.Exception.DatabaseException;
import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.DTO.ItemImgDTO;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 상품 저장과 이미지 관리 등을 하는 중앙 조정 서비스
 * 해당 서비스는 타 서비스간의 순환참조를 예방
 */
@Service
public class ItemManagementService {

    private final ItemService itemService;
    private final ItemImgService itemImgService;


    public ItemManagementService(ItemService itemService, ItemImgService itemImgService ) {
        this.itemService = itemService;
        this.itemImgService = itemImgService;
    }

    @Transactional
    public Long saveItemWithImages(ItemCreateRequest itemFormDto, List<MultipartFile> images) {
        // 2) 이미지 저장 + DTO 생성
        List<ItemImgDTO> itemImgDtoList = itemImgService.saveItemImgList(images);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        // 3) 실제 엔티티 생성 및 DB 저장
        //    itemService.saveItem()에서 내부적으로 itemRepository.save() 처리
        Item savedItem = itemService.saveItem(itemFormDto);

        // 4) 저장 결과 검증
        if (savedItem == null || savedItem.getId() == null) {
            throw new DatabaseException("아이템 저장 실패");
        }
        return savedItem.getId();
    }

}
