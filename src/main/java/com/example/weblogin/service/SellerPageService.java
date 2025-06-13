package com.example.weblogin.service;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.Exception.DatabaseException;
import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.dto.FileDTO;
import com.example.weblogin.domain.dto.request.ItemCreateRequest;
import com.example.weblogin.domain.file.FileCategory;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.file.File;
import com.example.weblogin.domain.itemOption.ItemOption;
import com.example.weblogin.domain.member.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerPageService {

	private final ItemRepository itemRepository; //상품
	private final CategorieRepository categorieRepository; //상품
	private final BrandRepository brandRepository; //상품
	private final ItemService itemService;
	private final FileService fileService;

	@Transactional
	public Long saveItemWithImages(ItemCreateRequest itemFormDto, List<MultipartFile> images) {

		// 카테고리/브랜드 조회
		Categorie category = categorieRepository.findCategorieById(itemFormDto.getCategory())
			.orElseThrow(() -> new DataNotFoundException("카테고리 정보를 찾을 수 없습니다."));

		Brand brand = brandRepository.findBrandById(itemFormDto.getBrand())
			.orElseThrow(() -> new DataNotFoundException("브랜드 정보를 찾을 수 없습니다."));

		Member member = MemberService.getCurrentUserMember(); //사용자 정보

		// ItemOptions
		List<ItemOption> itemOptionList = itemFormDto.getOptions().stream()
			.map(ItemOption::toEntity)    // DTO → 엔티티
			.collect(Collectors.toList());
		// Item Images
		List<FileDTO> fileDTOList = fileService.saveFileList(images);
		fileDTOList.forEach(fileDTO -> fileDTO.setTargetType(FileCategory.Item_Img.name()));
		List<File> files = fileDTOList.stream().map(File::toEntity).collect(Collectors.toList());


		Item item = Item.createItem(itemFormDto, category, brand, member, files, itemOptionList);

		// 5. DB에 저장
		itemRepository.save(item);//상품저장
		if (item == null || item.getId() == null) { // 저장 확인
			throw new DatabaseException();
		}
		return item.getId();
	}

	// 상품 수정
	@Transactional
	public Item updateItem(ItemCreateRequest itemFormDto, List<MultipartFile> images) {
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(ItemNotFoundException::new);
		item.updateItem(itemFormDto); //기존 필드 변경

		item.getOptions().clear(); //상품 옵션 재설정
		List<ItemOption> itemOptionList = itemFormDto.getOptions().stream()
			.map(ItemOption::toEntity)    // DTO → 엔티티
			.collect(Collectors.toList());
		for (ItemOption option : itemOptionList) {
			item.addOption(option);     // 연관관계 주인 설정, 컬렉션에 추가
		}

		List<File> oldFileList = item.getItemImages();
		item.getItemImages().clear();//상품 이미지 재설정
		oldFileList.forEach(file -> fileService.deleteImage(file.getId())); //이전 파일 삭제

		List<FileDTO> itemImgDtoList = fileService.saveFileList(images); //새로운 이미지 저장
		List<File> files = itemImgDtoList.stream().map(File::toEntity).collect(Collectors.toList());
		files.forEach(file -> item.addItemImg(file));

		return itemRepository.save(item);
	}

	/**
	 * 판매 중인 상품 조회
	 */
        public List<Item> getItemsOnSeller(Member member) {
                return itemRepository.findBySeller(member);
        }

	/**
	 * 상품 삭제
	 */
	@Transactional
	public void deleteItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

		if (item.getItemSellStatus() == ItemSellStatus.SELL) {
			throw new IllegalArgumentException("상품을 판매 중지 후 시도해주세요.");
		} else {
			itemRepository.deleteById(itemId);
		}

	}

	/**
	 * 상품 판매 중지
	 */
	@Transactional
	public void stopSellingItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

		if (item.getItemSellStatus() == ItemSellStatus.STOPPED) {
			throw new IllegalArgumentException("이미 판매 중지된 상품입니다.");
		} else {
			item.updateSellStatus(ItemSellStatus.STOPPED);
		}

	}

	/**
	 * 상품 판매 재개
	 */
	@Transactional
        public void resumeSellingItem(Long itemId) {
                Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

                if (item.getItemSellStatus() == ItemSellStatus.NOT_SALE
                        || item.getItemSellStatus() == ItemSellStatus.SOLD_OUT) {
                        item.updateSellStatus(ItemSellStatus.SELL);
                } else {
                        throw new IllegalArgumentException("이미 판매중인 상품입니다.");
                }

        }

        /**
         * 상품의 이미지 메타데이터 조회
         * @param itemId 상품 ID
         * @return 이미지 메타데이터 목록
         */
        @Transactional(readOnly = true)
        public List<FileDTO> getItemImages(Long itemId) {
                Item item = itemRepository.findById(itemId)
                        .orElseThrow(ItemNotFoundException::new);
                return item.getItemImages().stream()
                        .map(FileDTO::toDTO)
                        .collect(Collectors.toList());
        }
}
