package com.example.weblogin.controller.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.service.ItemImgService;
import com.example.weblogin.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/common")
@RestController
public class RestCommonController {
	@Autowired
	ItemImgService itemImgService;

	@Autowired
	ItemService itemService;

	/**
	 * 상품 이미지 저장
	 * @param file 상품 이미지 파일
	 * @param itemId 상품 id
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveImage")
	@Transactional
	public ResponseEntity<?> saveImg(@RequestParam(value = "itemId", required = false) Long itemId,
		@RequestParam("filepond") MultipartFile file) throws Exception {

		ItemImg itemImg = itemImgService.saveItemImg(file); // 수정된 메서드 사용
		if (itemId != null) {
			// 상품 수정 시에는 해당 이미지를 상품과 연결
			itemImgService.addImageToItem(itemId, itemImg); // 파일명 대신 이미지 ID 사용
		}

		return ResponseEntity.ok(itemImg); // 이미지 ID 목록을 응답으로 반환
	}

	/**
	 * 상품 이미지 로드
	 * @param itemId
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@GetMapping("/loadImage/{itemId}")
	public ResponseEntity<List<ItemImg>> loadImage(@PathVariable Long itemId) throws Exception {
		List<ItemImg> imageUrls = itemImgService.getImageUrls(itemId);
		return ResponseEntity.ok(imageUrls);
	}

	// @PostMapping("/mapingImage")
	// public String mapingImage(@RequestParam(value = "itemId", required = false) Long itemId) {
	//
	// }
}
