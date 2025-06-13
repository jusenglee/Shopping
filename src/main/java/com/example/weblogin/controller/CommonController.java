package com.example.weblogin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.itemOption.ColorType;
import com.example.weblogin.domain.itemOption.MaterialType;
import com.example.weblogin.domain.itemOption.SizeType;
import com.example.weblogin.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;
	private final FileService fileService;

	/**
	 * 상품정보 - 색상
	 *
	 * @return
	 */
	@GetMapping("/colors")
	public List<String> getColorList() {
		// Enum의 values()를 문자열로 변환
		return Arrays.stream(ColorType.values()).map(Enum::name).collect(Collectors.toList());
	}

	/**
	 * 상품정보 - 사이즈
	 *
	 * @return
	 */
	@GetMapping("/sizes")
	public List<String> getSizeList() {
		return Arrays.stream(SizeType.values()).map(Enum::name).collect(Collectors.toList());
	}

	/**
	 * 상품정보 - 소재
	 *
	 * @return
	 */
	@GetMapping("/materials")
	public List<String> getMaterialList() {
		return Arrays.stream(MaterialType.values()).map(Enum::name).collect(Collectors.toList());
	}


	/**
	 * 카테고리 정보(목록) 가져오기
	 *
	 * @return
	 */
	@GetMapping("/categories")
	public List<Categorie> getCategories() {
		return categorieRepository.findAll();
	}

	/**
	 * 브랜드 정보(목록) 가져오기
	 * @return
	 */
	@GetMapping("/brands")
	public List<Brand> getBrands() {
		return brandRepository.findAll();
	}

	/**
	 * DB에서 이미지를 찾아 외부로 내보내는 함수
	 * @param imageId
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@GetMapping("/getImage/{imageId}")
        public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
		byte[] imageBytes = fileService.loadFileAsResource(imageId);
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_JPEG)
			.body(imageBytes);
	}
}
