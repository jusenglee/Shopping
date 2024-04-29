package com.example.weblogin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.ItemImg.ItemImgRepository;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;

@Service
public class ItemImgService {
	private static final Logger logger = LogManager.getLogger(ItemImgService.class);
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	ItemImgRepository itemImgRepository;

	@Value("${itemImgLocation}")
	private String itemImgLocation;

	/**
	 * 상품 이미지 저장
	 * @param itemImgFile 실제 파일 데이터
	 * @throws Exception 예외처리
	 */
	@Transactional
	public ItemImg saveItemImg(MultipartFile itemImgFile) {
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";

		//파일 저장
		if (StringUtils.hasText(oriImgName)) {
			UUID uuid = UUID.randomUUID();
			String extension = StringUtils.getFilenameExtension(oriImgName);
			imgName = uuid + (extension != null ? "." + extension : "");
			String fileUploadFullUrl = itemImgLocation + "/" + imgName;

			try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
				fos.write(itemImgFile.getBytes());
				logger.info("상품 이미지 저장 완료, 경로 {}", fileUploadFullUrl);
				logger.info("저장된 이미지 이름 : {}", imgName);
			} catch (IOException e) {
				logger.error("이미지 파일 저장에 실패했습니다.", e);
				throw new IllegalStateException("파일 저장 실패: " + imgName, e);
			}
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
		logger.info("상품 이미지 저장 완료. 이미지 ID : {}", itemImg.getId());
		return itemImg;
	}

	/**
	 * 이미지에 상품 매핑하는 메소드
	 * @param itemId 상품 ID
	 * @param img 매핑할 이미지 객체
	 */
	@Transactional
	public void addImageToItem(Long itemId, ItemImg img) {
		Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
		img.setItem(item);
		img.setRepimgYn("Y");
		itemImgRepository.save(img);
		logger.info("상품 이미지 등록 완료. 상품 id :{}", itemId);
		logger.info("상품 이미지 등록 완료. 이미지 id :{}", img.getId());
	}

	/**
	 * 상품에 매핑된 이미지 불러오기
	 * @param itemId 상품 ID
	 * @return
	 */
	public List<ItemImg> getImageUrls(Long itemId) {
		return itemImgRepository.findByItemIdOrderByIdAsc(itemId);
	}

	/**
	 * 이미지를 외부로 내보내는 함수
	 * @param imageid
	 * @return
	 */
	public Resource loadFileAsResource(Long imageid) throws MalformedURLException {
		ItemImg itemImg = itemImgRepository.findById(imageid).orElseThrow(DataNotFoundException::new);
		Path filePath = Paths.get(itemImgLocation).resolve(itemImg.getImgName()).normalize();
		return new UrlResource(filePath.toUri());
	}

	/**
	 * 상품 이미지 삭제
	 * @param imageId 삭제할 이미지 ID
	 */
	@Transactional
	public void deleteImage(Long imageId) {
		// 데이터베이스에서 이미지 레코드 찾기
		ItemImg itemImg = itemImgRepository.findById(imageId)
			.orElseThrow(() -> new DataNotFoundException("해당 이미지를 찾을 수 없습니다."));

		// 파일 경로 구성
		File file = new File(itemImgLocation, itemImg.getImgName());
		// 파일이 존재하는 경우 삭제 시도
		if (file.exists()) {
			if (!file.delete()) {
				// 파일 삭제 실패를 로그로 남기지만, 예외는 발생시키지 않음
				logger.error("파일 삭제 실패: {}" + file.getAbsolutePath());
			}
		} else {
			// 파일이 존재하지 않는 경우, 로그만 남김
			logger.error("해당 이미지 파일을 찾을 수 없음: {}" + file.getAbsolutePath());
		}

		itemImgRepository.delete(itemImg);
		logger.info("이미지 삭제완료(DB) - {}", itemImg.getId());

	}
}
