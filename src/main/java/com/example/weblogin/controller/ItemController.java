package com.example.weblogin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.weblogin.domain.DTO.ItemCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.DTO.ItemResponseDTO;
import com.example.weblogin.domain.DTO.ItemSearchRequest;
import com.example.weblogin.domain.item.ItemRepositoryCustom;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.itemOption.ColorType;
import com.example.weblogin.domain.itemOption.MaterialType;
import com.example.weblogin.domain.itemOption.SizeType;
import com.example.weblogin.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final CategorieRepository categorieRepository;
    private final BrandRepository brandRepository;

    //상품 검색
    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponseDTO>> searchItems(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "brandIds", required = false) List<Long> brandIds, @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds, @RequestParam(value = "sortBy", required = false) ItemSearchRequest.SortBy sortBy, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemSearchRequest request = new ItemSearchRequest(name, brandIds, categoryIds, sortBy, page, size);
        Page<ItemResponseDTO> items = itemRepositoryCustom.search(request);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //모든 상품 찾기 - 인기순
    @GetMapping("/all")
    public ResponseEntity<Page<ItemResponseDTO>> getAllItems(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemSearchRequest request = new ItemSearchRequest(null, null, null, ItemSearchRequest.SortBy.POPULARITY, 0, 10);
        Page<ItemResponseDTO> items = itemRepositoryCustom.search(request);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/getItemDetails/{id}")
    public ResponseEntity<ItemCreateRequest> getItemDetail(@PathVariable long id) {
        return new ResponseEntity<>(itemService.getItemDetail(id), HttpStatus.OK);
    }
}
