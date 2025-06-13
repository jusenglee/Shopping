package com.example.weblogin.controller;

import java.util.List;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.domain.dto.request.ItemCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.dto.response.ItemResponseDTO;
import com.example.weblogin.domain.dto.request.ItemSearchRequest;
import com.example.weblogin.domain.file.File;
import com.example.weblogin.domain.file.FileCategory;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.service.FileService;
import com.example.weblogin.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final FileService fileService;
    private final CategorieRepository categorieRepository;
    private final BrandRepository brandRepository;


    @GetMapping("/getItemDetails/{itemId}")
    public ResponseEntity<ItemResponseDTO> getItemDetail(@PathVariable long itemId) {
        return new ResponseEntity<>(itemService.getItemDetail(itemId), HttpStatus.OK);
    }
}
