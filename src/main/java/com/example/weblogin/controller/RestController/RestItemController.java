package com.example.weblogin.controller.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.ItemSearchRequestDTO;
import com.example.weblogin.domain.DTO.MainItemDto;
import com.example.weblogin.domain.item.ItemRepositoryCustom;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestItemController {

	private final ItemService itemService;
	private final ItemRepositoryCustom itemRepositoryCustom;
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;

	// 상품 등록
	@PostMapping("/admin/newItemAjax")
	public ResponseEntity<?> itemSave(@ModelAttribute @Valid ItemFormDto itemFormDto,
		BindingResult bindingResult) throws
		Exception {
		Member member = MemberService.getCurrentUserMember();
		if (bindingResult.hasErrors()) {
			Map<String, Object> errors = bindingResult.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
			return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			itemFormDto.setAdmin(member);
			itemService.saveItem(itemFormDto);
			return new ResponseEntity<>("상품 등록 완료", HttpStatus.OK);
		}
	}

	@GetMapping("/categories")//카테고리 정보(목록) 가져오기
	public List<Categorie> getCategories() {
		return categorieRepository.findAll();
	}

	@GetMapping("/brands") // 브랜드 정보(목록) 가져오기
	public List<Brand> getBrands() {
		return brandRepository.findAll();
	}

	//상품 수정
	@PutMapping("/admin/manage/item/modify/{itemId}/pro")
	public String updateItem(@Valid ItemFormDto itemFormDto,
		@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
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
	public ResponseEntity<Page<MainItemDto>> searchItems(@RequestParam(value = "name", required = false) String name,
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
	public ResponseEntity<Page<MainItemDto>> getAllItems(@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "10") int size) {

		ItemSearchRequestDTO request = new ItemSearchRequestDTO(null, null, null,
			ItemSearchRequestDTO.SortBy.POPULARITY, 0, 10);
		Page<MainItemDto> items = itemRepositoryCustom.search(request);
		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	//상품 상세보기
	@GetMapping("item/{itemId}")
	public ResponseEntity<?> itemDetail(@PathVariable("itemId") Long itemId,
		@AuthenticationPrincipal Authentication authentication) {
		try {
			Map<String, Object> response = new HashMap<>();
			ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
			if (authentication != null) {
				Member member = MemberService.getCurrentUserMember();
				response.put("member", member);
			}
			response.put("itemFormDto", itemFormDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("상품을 찾을 수 없습니다.");
		}
	}
}
