package com.example.weblogin.controller;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import com.example.weblogin.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 판매자에 해당하는 페이지 관리
// 판매자페이지, 상품관리, 판매내역

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final SaleService saleService;
    private final BrandRepository brandRepository;

    private boolean isAdmin(Member member) {
        return member != null && member.getRole().equals(MemberRole.ADMIN);
    }

    private String redirectToMainIfNotAdmin(Member member) {
        if (!isAdmin(member)) {
            return "redirect:/main";
        }
        return null;
    }


    // 판매자 관리 페이지 접속
    @GetMapping("/myPage")
    public String myPage(Authentication authentication, Model model) {
        Member member = MemberService.getCurrentUserMember();
        String redirect = redirectToMainIfNotAdmin(member);

        if (redirect != null) return redirect;
        model.addAttribute("currentPage", "myPage");
        return "admin/sellerPage";

    }

    // 상품 등록 페이지
    @GetMapping("/newItem")
    public String newItem(Authentication authentication, Model model) {
        Member member = MemberService.getCurrentUserMember();
        String redirect = redirectToMainIfNotAdmin(member);

        if (redirect != null) return redirect;
        model.addAttribute("currentPage", "newItem");
        return "/admin/itemForm";

    }

    //상품 수정 페이지
    @GetMapping(value = "/manage/itemDetail")
    public String itemDetail(Authentication authentication, Model model, ItemFormDto itemFormDto) {
        Member member = MemberService.getCurrentUserMember();
        String redirect = redirectToMainIfNotAdmin(member);

        if (redirect != null) return redirect;
        return "item/itemForm";
    }


    // 상품 관리 페이지
    @GetMapping("/manage")
    public String itemManage(Authentication authentication, Model model) {
        Member member = MemberService.getCurrentUserMember();
        String redirect = redirectToMainIfNotAdmin(member);

        if (redirect != null) return redirect;
        model.addAttribute("currentPage", "manage");
        return "/admin/itemManage";

    }

    // 판매 내역 조회 페이지
    @GetMapping("/saleList")
    public String saleList(Authentication authentication, Model model) {
        Member member = MemberService.getCurrentUserMember();
        String redirect = redirectToMainIfNotAdmin(member);

        if (redirect != null) return redirect;
        model.addAttribute("currentPage", "manage");
        return "admin/itemManage";
    }
}
