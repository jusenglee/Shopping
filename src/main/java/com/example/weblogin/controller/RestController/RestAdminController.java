package com.example.weblogin.controller.RestController;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.service.ItemImgService;
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
	private final ItemImgService itemImgService;
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

	/**
	 *  상품 등록
	 * @param itemFormDto 상품정보
	 * @return ResponseEntity
	 * @throws Exception 예외처리
	 */
	// 상품 등록
	@PostMapping("/newItem")
	public ResponseEntity<?> itemSave(@RequestBody @Valid ItemFormDto itemFormDto) throws Exception {
		Member member = MemberService.getCurrentUserMember();
		try {
			itemFormDto.setAdmin(member);
			Long itemId = itemService.saveItem(itemFormDto);
			List<ItemImg> itemImgDtoList = itemFormDto.getItemImgDtoList();
			if (itemId != null && !itemImgDtoList.isEmpty()) {
				for (ItemImg itemImg : itemImgDtoList) {
					itemImgService.addImageToItem(itemId, itemImg); // 이미지 - 아이템 매핑
				}
			}
			return ResponseEntity.ok("상품 등록 완료");
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
	public ResponseEntity<?> updateItem(@RequestBody @Valid ItemFormDto itemFormDto) {
		try {
			itemService.updateItem(itemFormDto);
			return new ResponseEntity<>("상품 수정 완료.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("상품 수정에 실패했습니다. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
