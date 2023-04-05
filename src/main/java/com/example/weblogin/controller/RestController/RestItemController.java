package com.example.weblogin.controller.RestController;


import com.example.weblogin.config.auth.PrincipalDetails;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestItemController {

    private final ItemService itemService;


    // 상품 등록
    @PostMapping("/admin/manage/item/new")
    public void itemSave(ItemFormDto itemFormDto, @AuthenticationPrincipal PrincipalDetails principalDetails, List<MultipartFile> itemImgFileList)
            throws Exception {
        itemFormDto.setADMIN(principalDetails.getMember());
        itemService.saveItem(itemFormDto, itemImgFileList);
    }

    //상품 수정
    @PutMapping("/admin/manage/item/modify/{itemId}/pro")
    public String updateItem(@Valid ItemFormDto itemFormDto
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            return"첫번째 상품 이미지는 필수 입력 값입니다.";
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

    //메인페이지 상품목록 호출
    @GetMapping("/main/allItem/countview")
    public Page<Item> countview(int page) {
        Page<Item> items = itemService.getListLike(page);
        return items;
    }
}
