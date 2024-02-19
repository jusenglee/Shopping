package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.MemberFormDto;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;

import java.util.Objects;

// 구매자에 해당하는 페이지 관리
// 마이페이지, 회원정보수정, 장바구니, 주문, 주문취소

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserPageController {

	private final MemberService memberService;


	// 유저 페이지 접속
	@GetMapping("/myPage")
	public String userPage(@PathVariable("id") Long id, Model model,
		@AuthenticationPrincipal PrincipalDetails principalDetails) {
			return "/user/userPage";

	}

	// 회원 정보 수정페이지 접속
	@GetMapping("/modify")
	public String userModify(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		model.addAttribute("member", memberService.getMemberDetail(principalDetails.getMember().getId()));
		return "/userModify";
	}

	// 장바구니 페이지 접속
	@GetMapping("/cart")
	public String userCartPage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return "/user/userCart";
	}

	// 주문 내역 조회 페이지
	@GetMapping("/orderHist")
	public String orderList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		return "user/userOrderList";
	}

	// 장바구니 상품 주문
	@GetMapping("/cart/checkout")
	public String cartCheckout(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
		return "redirect:/user/cart";
	}

	// 상품 개별 주문 -> 상품 상세페이지에서 구매하기 버튼으로 주문
	@GetMapping("/checkout/order")
	public String checkout(@PathVariable("itemId") Integer itemId) {
		return "redirect:/user/orderHist";
	}

}
