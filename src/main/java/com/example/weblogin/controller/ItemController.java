package com.example.weblogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
	private final BrandRepository brandRepository;

	// 상품 상세 페이지
	@GetMapping("/item/view/{itemId}")
	public String itemView(@PathVariable("itemId") Long itemId) {
		return "itemView2";
	}

	@Controller
	public class VueController {
		@GetMapping("/")
		public String showVuePage() {
			return "/index.html";
		}
	}
}

