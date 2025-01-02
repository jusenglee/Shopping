package com.example.weblogin.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.itemImg.ItemImg;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	private final SaleService saleService;
	private final ItemService itemService;
	private final ItemImgService itemImgService;
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;


	/**
	 *  상품 등록
	 * @param itemFormDtoString 상품정보의 JSON 데이터
	 * @return ResponseEntity
	 * @throws Exception 예외처리
	 */
	// 상품 등록
	@PostMapping(value = "/newItem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public ResponseEntity<?> itemSave(
	    @RequestPart("itemFormDto") String itemFormDtoString,  // JSON 문자열
	    @RequestPart(value = "images", required = false) List<MultipartFile> images // 이미지 파일
	) {
	    try {
	        // 1) JSON 문자열 -> ItemFormDto 객체로 역직렬화
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	        ItemCreateRequest itemFormDto = objectMapper.readValue(itemFormDtoString, ItemCreateRequest.class);

	        // 2) 로그인 유저(Member) 셋팅
	        Member member = MemberService.getCurrentUserMember();
	        itemFormDto.setAdmin(member);

	        // 3) 아이템 저장(기존 로직 재활용)
	        Long itemId = itemService.saveItem(itemFormDto);

	        // 4) 넘어온 MultipartFile 리스트를 저장 혹은 DB 매핑
	        if (images != null && !images.isEmpty()) {

	            for (MultipartFile image : images) {
					ItemImg itemImg = itemImgService.saveItemImg(image); //이미지 저장
					itemImgService.addImageToItem(itemId,itemImg); // 이미지 매핑
	            }
	        }

	        return ResponseEntity.ok("상품 등록 완료");
	    } catch (Exception e) {
	        e.printStackTrace();
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
	public ResponseEntity<?> updateItem(@RequestBody @Valid ItemCreateRequest itemFormDto) {
		try {
			itemService.updateItem(itemFormDto);
			return new ResponseEntity<>("상품 수정 완료.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("상품 수정에 실패했습니다. 다시 시도해주세요", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
