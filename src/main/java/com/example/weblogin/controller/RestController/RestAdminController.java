package com.example.weblogin.controller.RestController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.ItemService;
import com.example.weblogin.service.MemberService;
import com.example.weblogin.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RestAdminController {

	private final SaleService saleService;
	private final ItemService itemService;
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;

	@GetMapping("/categories")//카테고리 정보(목록) 가져오기
	public List<Categorie> getCategories() {
		return categorieRepository.findAll();
	}

	@GetMapping("/brands") // 브랜드 정보(목록) 가져오기
	public List<Brand> getBrands() {
		return brandRepository.findAll();
	}

	// 상품 등록
	@PostMapping("/newItem")
	public ResponseEntity<?> itemSave(@ModelAttribute @Valid ItemFormDto itemFormDto) throws Exception {
		Member member = MemberService.getCurrentUserMember();
		try {
			itemFormDto.setAdmin(member);
			itemService.saveItem(itemFormDto);
			return new ResponseEntity<>("상품 등록 완료", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("상품 등록 실패. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 판매 상품 전체 조회
	@GetMapping("/ItemManage/sellerItemList")
	public List<Item> saleList() {
		Member member = MemberService.getCurrentUserMember();
		List<Item> Item = saleService.getItemsOnSaller(member);
		return Item;
	}

	// 판매내역 전체 조회
	@PostMapping("/manage/salelist")
	public List<SaleInfo> saleList(@PathVariable("id") Long id) {
		return saleService.getSaleInfoList();
	}

	//상품 판매중지
	@RequestMapping("manage/delete/{itemId}")
	public void itemDelete(@PathVariable("itemId") Long id) {
		saleService.stopSellingItem(id);
	}

	//상품 수정
	@PostMapping("/modifyItem")
	public ResponseEntity<?> updateItem(@Valid ItemFormDto itemFormDto,
		@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
		if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
			return new ResponseEntity<>("최소 1개의 상품 이미지가 필요합니다.", HttpStatus.BAD_REQUEST);
		}
		try {
			itemService.updateItem(itemFormDto, itemImgFileList);
		} catch (Exception e) {
			return new ResponseEntity<>("상품 등록에 실패했습니다. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("최소 1개의 상품 이미지가 필요합니다.", HttpStatus.BAD_REQUEST);
	}

	//상품 삭제 (중지 ->)
	@DeleteMapping("/manage/delete/{itemId}")
	public void stopSellingItem(@PathVariable("itemId") Long id) {
		itemService.deleteItem(id);
	}

}
