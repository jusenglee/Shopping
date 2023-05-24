package com.example.weblogin.controller.RestController;


import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepositoryCustom;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Kategorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.member.MemberRole;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestItemController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final ItemRepositoryCustom itemRepositoryCustom;
    private final CategorieRepository categorieRepository;
    private final BrandRepository brandRepository;

        // 상품 등록
        @PostMapping("/admin/manage/item/new")
        public ResponseEntity<?> itemSave(@Valid @ModelAttribute ItemFormDto itemFormDto, BindingResult bindingResult,
                                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Authentication authentication)
                throws Exception {
                 Member member = memberService.getAdminAuthenticatedMember(authentication);
                if (bindingResult.hasErrors()) {
                Map<String, Object> errors = bindingResult.getFieldErrors().stream()
                        .collect(
                                Collectors.toMap(FieldError::getField , FieldError::getDefaultMessage)
                        );
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
                }else {
                    itemFormDto.setADMIN(member);
                    itemService.saveItem(itemFormDto, itemImgFileList);
                    return new ResponseEntity<>("상품 등록 완료", HttpStatus.OK);
                }
        }

    @GetMapping("/categories")//카테고리 정보(목록) 가져오기
    public List<Kategorie> getCategories() {
        return categorieRepository.findAll();
    }

    @GetMapping("/brands") // 브랜드 정보(목록) 가져오기
    public List<Brand> getBrands() {
        return brandRepository.findAll();
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
    //모든 상품 찾기 - 인기순
    @GetMapping("/all")
    public ResponseEntity<Page<MainItemDto>> getAllItems(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        ItemSearchRequestDTO request = new ItemSearchRequestDTO(null, null, null, ItemSearchRequestDTO.SortBy.POPULARITY, 0, 10);
        Page<MainItemDto> items = itemRepositoryCustom.search(request);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //상품 상세보기
    @GetMapping("item/{itemId}")
    public ResponseEntity<?> itemDetail(@PathVariable("itemId") Long itemId) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            return new ResponseEntity<>(itemFormDto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("상품을 찾을 수 없습니다.");
        }
    }
}
