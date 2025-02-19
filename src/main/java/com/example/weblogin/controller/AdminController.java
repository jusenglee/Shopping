package com.example.weblogin.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.weblogin.domain.DTO.ItemResponseDTO;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.member.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SaleService saleService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;
    private final ItemManagementService itemManagementService;

    /**
     * 상품 등록
     *
     * @param itemFormDto 상품정보의 데이터
     * @return ResponseEntity
     * @throws Exception 예외처리
     */
    // 상품 등록
    @PostMapping(value = "/newItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> itemSave(@Valid @RequestPart("itemFormDto") ItemCreateRequest itemFormDto,  // 상품 데이터
                                      @Valid @NotNull(message = "상품 이미지는 필수 입력 값입니다. ") @RequestPart(value = "images", required = false) List<MultipartFile> images // 상품 이미지
    ) {
        // 3) 아이템 저장(기존 로직 재활용)
        Long itemId = itemManagementService.saveItemWithImages(itemFormDto, images);
        return ResponseEntity.ok(itemId);
    }

    // 판매 상품 전체 조회
    @GetMapping("/ItemManage/sellerItemList")
    public List<ItemResponseDTO> saleList() {
        Member member = MemberService.getCurrentUserMember();
        List<Item> items = saleService.getItemsOnSaller(member);
        List<ItemResponseDTO> response = items.stream().map( ItemResponseDTO::toDTO).collect(Collectors.toList());
        return response;
    }

    // 판매내역 전체 조회
    @PostMapping("/manage/salelist")
    public List<SaleInfo> saleList(@PathVariable("id") Long id) {
        return saleService.getSaleInfoList();
    }

    //상품 삭제
    @RequestMapping("manage/delete/{itemId}")
    public void itemDelete(@PathVariable("itemId") Long id) {
        saleService.deleteItem(id);
    }

    //상품 판매중지
    @RequestMapping("manage/stopSellItem/{itemId}")
    public ResponseEntity<?> stopSellItem(@PathVariable("itemId") Long id) {
        saleService.stopSellingItem(id);
        return new ResponseEntity<>("상품 판매중지 완료", HttpStatus.OK);
    }

    //상품 판매중지
    @RequestMapping("manage/resumeSellingItem/{itemId}")
    public void resumeSellingItem(@PathVariable("itemId") Long id) {
        saleService.resumeSellingItem(id);
    }

    //상품 수정
    @PostMapping("/modifyItem")
    public ResponseEntity<?> updateItem(@Valid @RequestPart("itemFormDto") ItemCreateRequest itemFormDto ,
        @Valid @NotNull(message = "상품 이미지는 필수 입력 값입니다. ") @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            itemService.updateItem(itemFormDto, images);
            return new ResponseEntity<>("상품 수정 완료.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("상품 수정에 실패했습니다. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
