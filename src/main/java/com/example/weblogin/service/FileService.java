package com.example.weblogin.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.example.weblogin.domain.dto.FileDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.domain.file.File;
import com.example.weblogin.domain.file.FileCategory;
import com.example.weblogin.domain.file.FileRepository;
import com.example.weblogin.domain.item.ItemRepository;

@Service
public class FileService {
	private static final Logger logger = LogManager.getLogger(FileService.class);
	@Autowired
	ItemRepository itemRepository;
	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	FileRepository fileRepository;

	@Value("${itemImgLocation}")
	private String itemImgLocation;


	@Transactional
	public List<FileDTO> saveFileList(List<MultipartFile> files) {
		List<FileDTO> fileDTOList = Optional.ofNullable(files)
			.orElse(List.of())
			.stream()
			.map(this::saveFile) // MultipartFile → FileDTO
			.collect(Collectors.toList());

		return fileDTOList;
	}

	/**
	 * 상품 이미지 저장
	 * @param itemImgFile 실제 파일 데이터
	 * @throws Exception 예외처리
	 */
	@Transactional
	public FileDTO saveFile(MultipartFile itemImgFile) {
		String originName = itemImgFile.getOriginalFilename();
		String savedName = "";
		String url = "";

		//파일 저장
		if (StringUtils.hasText(originName)) {
			UUID uuid = UUID.randomUUID();
			String extension = StringUtils.getFilenameExtension(originName);
			savedName = uuid + (extension != null ? "." + extension : "");
			String fileUploadFullUrl = itemImgLocation + "/" + savedName;

			try (FileOutputStream fos = new FileOutputStream(fileUploadFullUrl)) {
				fos.write(itemImgFile.getBytes());
				logger.info("상품 이미지 저장 완료, 경로 {}", fileUploadFullUrl);
				logger.info("저장된 이미지 이름 : {}", savedName);
			} catch (IOException e) {
				logger.error("이미지 파일 저장에 실패했습니다.", e);
				throw new IllegalStateException("파일 저장 실패: " + savedName, e);
			}
			url = "/image/" + savedName;
		}
		//상품 이미지 정보 저장
        return FileDTO.builder()
			.savedName(savedName)
			.oriImgName(originName)
			.fileUrl(url)
			.build();
	}

	/**
	 * 파일을 외부로 내보내는 함수
	 * @param
	 * @return
	 */
	public  byte[] loadFileAsResource(Long fileId) throws MalformedURLException {
		File file = fileRepository.findById(fileId).orElseThrow(DataNotFoundException::new);

		Path filePath = Paths.get(itemImgLocation).resolve(file.getFileUrl()).normalize();
		java.io.File data = new java.io.File(filePath.toString());

		try (FileInputStream fis = new FileInputStream(data)) {
			return fis.readAllBytes();
		} catch (IOException e) {
			throw new RuntimeException("이미지 파일을 읽을 수 없습니다.", e);
		}
	}

	/**
	 * 상품 이미지 삭제
	 * @param imageId 삭제할 이미지 ID
	 */
	@Transactional
	public void deleteImage(Long imageId) {
		// 데이터베이스에서 이미지 레코드 찾기
		File itemImg = fileRepository.findById(imageId)
			.orElseThrow(() -> new DataNotFoundException("해당 이미지를 찾을 수 없습니다."));

		// 파일 경로 구성
		java.io.File file = new java.io.File(itemImgLocation, itemImg.getFileUrl());
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

		fileRepository.delete(itemImg);
		logger.info("이미지 삭제완료(DB) - {}", itemImg.getId());

	}

}
