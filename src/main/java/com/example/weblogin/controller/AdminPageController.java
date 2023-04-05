package com.example.weblogin.controller;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.SaleService;
import com.example.weblogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

// 판매자에 해당하는 페이지 관리
// 판매자페이지, 상품관리, 판매내역

@RequiredArgsConstructor
@Controller
public class AdminPageController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final SaleService saleService;


    // 판매자 관리 페이지 접속
    @GetMapping("/admin/{id}")
    public String sellerPage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            // 로그인이 되어있는 판매자의 id와 판매자 페이지에 접속하는 id가 같아야 함
            model.addAttribute("admin", memberService.findUser(id));

            return "/admin/sellerPage";
        } else {
            return "redirect:/main";
        }
    }


    // 상품 등록 페이지
    @GetMapping("/amdin/item/new")
    public String itemSaveForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails.getMember().getRole().equals("ROLE_SELLER")) {
            // 판매자

            model.addAttribute("itemFormDto", new ItemFormDto());
            return "/seller/itemForm";
        } else {
            // 일반 회원이면 거절 -> main
            return "redirect:/main";
        }
    }

    //상품 수정 페이지
    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }




    // 상품 관리 페이지
    @GetMapping("/admin/{id}/manage/")
    public String itemManage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails.getMember().getId() == id) {
            // 로그인이 되어있는 판매자의 id와 상품관리 페이지에 접속하는 id가 같아야 함
            model.addAttribute("admin", memberService.findUser(id));

            return "admin/itemManage";
        } else {
            return "redirect:/main";
        }
    }
    // 판매 내역 조회 페이지
    @GetMapping("/admin/salelist/{id}")
    public ModelAndView saleList(@PathVariable("id")Integer id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ModelAndView mv = new ModelAndView();
        // 로그인이 되어있는 유저의 id와 판매내역에 접속하는 id가 같아야 함
        if (Objects.equals(principalDetails.getMember().getId(), id)) {
            List<Item> saleItemList = saleService.getItemsOnSale();
            mv.addObject("sellerSaleItems", saleItemList);
            mv.setViewName("seller/itemManage");
            return mv;
        }
        else {
            mv.setViewName("redirect:/main");
            return mv;
        }
    }
}
