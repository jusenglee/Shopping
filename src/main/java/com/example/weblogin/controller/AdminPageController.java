package com.example.weblogin.controller;

import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import com.example.weblogin.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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


    // 판매자 관리 페이지 접속
    @GetMapping("mypage")
    public String sellerPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        // PrincipalDetails에서 필요한 정보 추출
        Long userId = principalDetails.getMember().getId();
        // 로그인이 되어있는 판매자의 id와 판매자 페이지에 접속하는 id가 같아야 함
        model.addAttribute("admin", memberService.findUser(userId));

        return "admin/sellerPage";
    }


    // 상품 등록 페이지
    @GetMapping("/newitem")
    public String itemSaveForm(Authentication authentication, Model model) {
        Member member = memberService.getAuthenticatedMember(authentication);
        if (member != null && member.getRole().equals(MemberRole.ADMIN)) {
            // 판매자
            return "/admin/itemForm";
        }
        // 일반 회원이거나 인증되지 않은 사용자이면 거절 -> main
        return "redirect:/main";
    }

    //상품 수정 페이지
    @GetMapping(value = "/item/{itemId}")
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
    @GetMapping("/manage/")
    public String itemManage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        return "admin/itemManage";
    }

    // 판매 내역 조회 페이지
    @GetMapping("/salelist")
    public ModelAndView saleList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ModelAndView mv = new ModelAndView();

        List<Item> saleItemList = saleService.getItemsOnSale();
        mv.addObject("sellerSaleItems", saleItemList);
        mv.setViewName("admin/itemManage");
        return mv;
    }
}
