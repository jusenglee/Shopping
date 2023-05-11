package com.example.weblogin.controller;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // 메인 페이지 html 하나로 통일
    // 메인 페이지 (로그인 안 한 유저) /localhost:8080
    @GetMapping("main")
    public String mainPage(Model model, Authentication authentication) {
        //로그인 처리에 대해 확인
        return "mainPage";
    }


    // 상품 상세 페이지
    @GetMapping("/item/view/{itemId}")
    public String ItemView(@PathVariable("itemId") Long itemId, Model model) {

        //제품 상세내용은 화면 변동이 없으므로 model을 통해 호출시 데이터 삽입
        ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
        model.addAttribute("item", itemFormDto);
            return "itemView";
    }

    }

//
//    // 검색 상품 리스트 페이지
//    @GetMapping("/item/list/")
//    public String itemList() {
//        return "itemList";
//    }

