package com.example.weblogin.controller;


import com.example.weblogin.domain.itemCategory.BrandRepository;
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
    private final BrandRepository brandRepository;

    @GetMapping("/")
    public String redirectToMain() {
        return "redirect:/main";
    }

    // 메인 페이지 html 하나로 통일
    // 메인 페이지 (로그인 안 한 유저) /localhost:8080
    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        return "mainPage";
    }


    // 상품 상세 페이지
    @GetMapping("/item/view/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId) {
            return "itemView2";
    }
    }

