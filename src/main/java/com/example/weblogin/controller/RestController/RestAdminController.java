package com.example.weblogin.controller.RestController;

import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.MemberService;
import com.example.weblogin.service.SaleService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestAdminController {

	private final SaleService saleService;

	// 판매 상품 전체 조회
	@GetMapping("/admin/manage/sellerItemList")
	public List<Item> saleList(Authentication authentication) {
        Member member = MemberService.getCurrentUserMember();
		return saleService.getItemsOnSaller(member);
	}

	// 판매내역 전체 조회
	@PostMapping("/admin/manage/salelist/")
	public List<SaleInfo> saleList(@PathVariable("id") Long id) {
		return saleService.getSaleInfoList();
	}

	//상품 판매중지
	@RequestMapping("/admin/manage/item/delete/{itemId}")
	public void itemDelete(@PathVariable("itemId") Long id) {
		saleService.stopSellingItem(id);
	}

}
