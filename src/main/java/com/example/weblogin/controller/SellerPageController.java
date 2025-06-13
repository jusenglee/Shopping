package com.example.weblogin.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.weblogin.domain.dto.request.SellerInfoUpdateRequest;
import com.example.weblogin.domain.dto.response.SellerInfoResponseDTO;
import com.example.weblogin.domain.dto.response.ItemResponseDTO;
import com.example.weblogin.service.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.dto.request.ItemCreateRequest;
import com.example.weblogin.domain.dto.FileDTO;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.member.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerPageController {

	private final SellerPageService sellerPageService;
	private final ItemService itemService;
	private final MemberService memberService;
	private final FileService fileService;

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
		@Valid @NotNull(message = "상품 이미지는 필수 입력 값입니다. ") @RequestPart(value = "images", required = false) List<MultipartFile> images) {

		Long itemId = sellerPageService.saveItemWithImages(itemFormDto, images); // 상품과 상품 이미지 저장
		return new ResponseEntity<>(itemId, HttpStatus.CREATED);
	}

	/**
	 * 상품 관리 페이지  - 등록한 상품 목록 호출
	 * @return
	 */
	@GetMapping("/itemManage/sellerItemList")
	public List<ItemResponseDTO> saleList() {
		Member member = MemberService.getCurrentUserMember(); //세션으로부터 사용자 검증 및 정보 가져오기
		List<Item> items = sellerPageService.getItemsOnSaller(member); // 등록한 item 목록 select
		return items.stream()
			.map(ItemResponseDTO::toDTO)
			.collect(Collectors.toList()); //ResponseDTO로 이루어진 List 형태로 변환하여 반환
	}

	/**
	 * 상품 관리 페이지 - 상품 판매중지
	 * @param id
	 * @return
	 */
	@PostMapping("manage/stopSellItem/{itemId}")
	public ResponseEntity<?> stopSellItem(@PathVariable("itemId") Long id) {
		sellerPageService.stopSellingItem(id);
		return new ResponseEntity<>("상품 판매중지 완료", HttpStatus.OK);
	}

	/**
	 * 상품 관리 페이지 - 상품 삭제
	 * @param id
	 */
	//상품 삭제
	@DeleteMapping("manage/delete/{itemId}")
	public void itemDelete(@PathVariable("itemId") Long id) {
		sellerPageService.deleteItem(id);
	}

	/**
	 * 상품 관리 페이지 - 상품 재판매
	 * @param id
	 */
	@PostMapping("manage/resumeSellingItem/{itemId}")
	public void resumeSellingItem(@PathVariable("itemId") Long id) {
		sellerPageService.resumeSellingItem(id);
	}

	/**
	 * 상품 정보 업데이트
	 * @param itemFormDto
	 * @param images
	 * @return
	 */
	@PostMapping("/modifyItem")
	public ResponseEntity<?> updateItem(@Valid @RequestPart("itemFormDto") ItemCreateRequest itemFormDto,
		@Valid @NotNull(message = "상품 이미지는 필수 입력 값입니다. ") @RequestPart(value = "images", required = false) List<MultipartFile> images) {
		Item item = sellerPageService.updateItem(itemFormDto, images);
		return new ResponseEntity<>(ItemResponseDTO.toDTO(item), HttpStatus.OK);
	}

	/**
	 * 사용자 (Seller)정보 수정 페이지 - 사용자 정보 호출
	 * @return
	 */
	@GetMapping("/sellerInfo")
	public ResponseEntity<?> sellerInfo() {
		Member member = MemberService.getCurrentUserMember();//세션에서 데이터 조회
		SellerInfoResponseDTO sellerInfo = new SellerInfoResponseDTO(member);// 엔티티에서 DTO로 변환하여 반환
		return ResponseEntity.ok(sellerInfo);
	}

	/**
	 * 사용자 (Seller)정보 수정 페이지 - 사용자 정보 업데이트
	 * @return
	 */
        @PostMapping("/sellerInfoUpdate")
        public ResponseEntity<?> updateSellerInfo(@Valid @RequestBody SellerInfoUpdateRequest requestDTO) {
                Member member = MemberService.getCurrentUserMember();//세션에서 데이터 조회
                memberService.updateSellerInfo(member, requestDTO);
                return ResponseEntity.ok(member);
        }

        /**
         * 특정 상품의 이미지 메타데이터 조회
         * @param itemId 조회할 상품 ID
         * @return 이미지 메타데이터 목록
         */
        @GetMapping("/item/{itemId}/images")
        public List<FileDTO> getItemImages(@PathVariable Long itemId) {
                return sellerPageService.getItemImages(itemId);
        }
}
