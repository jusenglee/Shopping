package com.example.weblogin.controller.RestController;


import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepositoryCustom;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final ItemRepositoryCustom itemRepositoryCustom;


    // 상품 등록
    @PostMapping("/admin/manage/item/new")
    public int itemSave(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                        @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Authentication authentication)
            throws Exception {
        ResponseEntity<Member> memberResponse = memberService.getUser(authentication);
        if (memberResponse.getStatusCode() == HttpStatus.OK) {
            Member member = memberResponse.getBody();

            if (bindingResult.hasErrors()) {
                return 401; //001 반환시 item/itemForm
            }

            if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
                return 402; //002 반환시 뷰에서 첫번째 상품 이미지는 필수 입력 값입니다. 알림 출력 후 리턴
            }

            try {
                itemFormDto.setADMIN(member);
                itemService.saveItem(itemFormDto, itemImgFileList);
                return 400;
            } catch (Exception e) {
                return 403; //002 반환시 뷰에서 상품 등록 중 에러가 발생하였습니다.  알림 출력 후 리턴
            }
        }
        return 0;
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //상품 수정
    @PutMapping("/admin/manage/item/modify/{itemId}/pro")
    public String updateItem(@Valid ItemFormDto itemFormDto
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            return "첫번째 상품 이미지는 필수 입력 값입니다.";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            return "상품 수정 중 에러가 발생하였습니다.";
        }
        return null;
    }

    //상품 삭제 (중지 ->)
    @DeleteMapping("/admin/manage/item/delete/{itemId}")
    public void stopSellingItem(@PathVariable("itemId") Long id) {
        itemService.deleteItem(id);
    }

    //메인페이지 상품목록 호출 - 날짜순 정렬
    @GetMapping("/main/allItem")
    public Page<Item> allItemView(int page) {
        Page<Item> items = itemService.getListByCreateDate(page);
        return items;
    }
//
//    //메인페이지 상품목록 호출
//    @GetMapping("/main/allItem/countview")
//    public Page<Item> countview(int page) {
//        Page<Item> items = itemService.getListLike(page);
//        return items;
//    }

    //상품 검색
    @GetMapping("/search")
    public ResponseEntity<Page<MainItemDto>> searchItems(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brandIds", required = false) List<Long> brandIds,
            @RequestParam(value = "categoryIds", required = false) List<Long> categoryIds,
            @RequestParam(value = "sortBy", required = false) ItemSearchRequestDTO.SortBy sortBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemSearchRequestDTO request = new ItemSearchRequestDTO(name, brandIds, categoryIds, sortBy, page, size);
        Page<MainItemDto> items = itemRepositoryCustom.search(request);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Page<MainItemDto>> getAllItems(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemSearchRequestDTO request = new ItemSearchRequestDTO(null, null, null, ItemSearchRequestDTO.SortBy.POPULARITY, 0, 10);
        Page<MainItemDto> items = itemRepositoryCustom.search(request);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
