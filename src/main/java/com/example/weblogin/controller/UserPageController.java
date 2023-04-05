package com.example.weblogin.controller;


import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.MemberFormDto;

import com.example.weblogin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import javax.persistence.EntityNotFoundException;

import java.util.Objects;

// 구매자에 해당하는 페이지 관리
// 마이페이지, 회원정보수정, 장바구니, 주문, 주문취소

@RequiredArgsConstructor
@Controller
public class UserPageController {

    private final MemberService memberService;


    // 유저 페이지 접속
    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 유저 페이지에 접속하는 id가 같아야 함
//        if (Objects.equals(principalDetails.getMember().getId(), id)) {

            model.addAttribute("user", memberService.findUser(id));

            return "/user/userPage";
//        } else {
//            return "redirect:/main";
//        }
    }

    // 회원 정보 수정페이지 접속
    @GetMapping("/user/modify/{id}")
    public String userModify(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 수정페이지에 접속하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {

            try {
                MemberFormDto MemberFormDto = memberService.getMemberDetail(id);
                model.addAttribute("itemFormDto", MemberFormDto);
            } catch (EntityNotFoundException e) {
                model.addAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");

                return "/userModify";
            }
        } else {
            model.addAttribute("errorMessage", "회원 정보가 다릅니다..");
            return "redirect:/main";
        }
        return "redirect:/main";
    }

    // 장바구니 페이지 접속
    @GetMapping("/user/cart/{id}")
    public String userCartPage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인이 되어있는 유저의 id와 장바구니에 접속하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {

            return "/user/userCart";
        }
        else {
            return "redirect:/main";
        }
    }

    // 주문 내역 조회 페이지
    @GetMapping("/user/orderHist/{id}")
    public String orderList(@PathVariable("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        // 로그인이 되어있는 유저의 id와 주문 내역에 접속하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {
            //주문내역 정보 로직은 Rest컨트롤러로 받기
            return "user/userOrderList";
        }
        // 로그인 id와 주문 내역 접속 id가 같지 않는 경우
        else {
            return "redirect:/main";
        }
    }

    // 장바구니 상품 주문

    @GetMapping("/user/cart/checkout/{id}")
    public String cartCheckout(@PathVariable("id") Long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        // 로그인이 되어있는 유저의 id와 주문하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {
            return "redirect:/user/cart/{id}";
        } else {
            return "redirect:/main";
        }
    }

    // 상품 개별 주문 -> 상품 상세페이지에서 구매하기 버튼으로 주문

    @GetMapping("/user/{id}/checkout/{itemId}")
    public String checkout(@PathVariable("id") Long id, @PathVariable("itemId") Integer itemId, @AuthenticationPrincipal PrincipalDetails principalDetails, int count) {
        // 로그인이 되어있는 유저의 id와 주문하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {

            return "redirect:/user/orderHist/{id}";
        } else {
            return "redirect:/main";
        }
    }



}
