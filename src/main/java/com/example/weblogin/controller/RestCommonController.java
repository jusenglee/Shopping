package com.example.weblogin.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.domain.itemImg.ItemImg;
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
	 * 상품 이미지 로드
	 * @param itemId
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@GetMapping("/loadImage/{itemId}")
	public ResponseEntity<List<ItemImg>> loadImage(@PathVariable Long itemId) throws DataNotFoundException {
		List<ItemImg> imageUrls = itemImgService.getImageUrls(itemId);
		return ResponseEntity.ok(imageUrls);
	}

	/**
	 * 상품 이미지 삭제
	 * @param imageId
	 * @return
	 */
	@DeleteMapping("/deleteImage/{imageId}")
	public ResponseEntity<String> deleteImage(@PathVariable Long imageId) throws IOException {
		itemImgService.deleteImage(imageId);
		return ResponseEntity.ok().body("이미지 삭제 완료.");
	}
}
