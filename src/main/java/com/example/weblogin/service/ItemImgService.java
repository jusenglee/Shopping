package com.example.weblogin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.ItemImg.ItemImgRepository;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;

@Service
public class ItemImgService {

	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	ItemImgRepository itemImgRepository;
	@Autowired
	FileService fileService;

	@Value("${itemImgLocation}")
	private String itemImgLocation;

	/**
	 * 상품 이미지 저장
	 * @param itemImgFile 실제 파일 데이터
	 * @throws Exception 예외처리
	 */
	public ItemImg saveItemImg(MultipartFile itemImgFile) throws Exception {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";

		//파일 업로드
		if (StringUtils.hasText(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/image/" + imgName;
		}

		//상품 이미지 정보 저장
		ItemImg itemImg = ItemImg.builder()
			.imgName(imgName)
			.oriImgName(oriImgName)
			.repimgYn("N")
			.imgUrl(imgUrl)
			.build();
		itemImgRepository.save(itemImg);
		itemImgRepository.findById(itemImg.getId());
		return itemImg;
	}

	/**
	 * 상품에 이미지를 추가하는 메서드
	 */
	public void addImageToItem(Long itemId, ItemImg img) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid Item Id: " + itemId));
		img.setItem(item);
		img.setRepimgYn("Y");
		itemImgRepository.save(img);
	}

	/**
	 * 상품 이미지 불러오기
	 * @param itemId
	 * @return
	 */
	public List<ItemImg> getImageUrls(Long itemId) {
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		return itemImgList;
	}
}
