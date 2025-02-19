package com.example.weblogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.weblogin.domain.DTO.ItemImgDTO;
import com.example.weblogin.domain.DTO.ItemOptionRequest;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.itemOption.ItemOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.orderItem.OrderItemRepository;
import com.example.weblogin.domain.saleitem.SaleItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;

    private final OrderItemRepository orderItemRepository;

    private final SaleItemRepository saleItemRepository;
    private final CategorieRepository categorieRepository;
    private final BrandRepository brandRepository;

    /**
     * 상품 저장
     *
     * @param dto ItemCreateRequest 상품 저장을 위해 클라이언트로부터 데이터를 받아오는 DTO
     * @return
     */
    @Transactional
    public Item saveItem(ItemCreateRequest dto) {
        // 1. 카테고리/브랜드 조회
        Categorie category = categorieRepository.findCategorieById(dto.getCategory())
                .orElseThrow(() -> new DataNotFoundException("카테고리 정보를 찾을 수 없습니다."));
        Brand brand = brandRepository.findBrandById(dto.getBrand())
                .orElseThrow(() -> new DataNotFoundException("브랜드 정보를 찾을 수 없습니다."));
        Member member = MemberService.getCurrentUserMember();

        Item item = Item.toEntity(dto, category, brand, member);

        // 3. 옵션 추가 (옵션이 있으면)
        if (dto.getOptions() != null) {
			// Service 레이어에서 엔티티 변환
			List<ItemOption> itemOptionList = dto.getOptions().stream()
					.map(ItemOption::toEntity)    // DTO → 엔티티
					.collect(Collectors.toList());

			// 생성된 엔티티를 Item에 추가
			for (ItemOption option : itemOptionList) {
				item.addOption(option);     // 연관관계 주인 설정, 컬렉션에 추가
			}
        }

        // 4. 이미지 DTO -> 엔티티 변환, 연관관계 설정
        if (dto.getItemImgDtoList() != null) {
            // Service 레이어에서 엔티티 변환
            List<ItemImg> itemImgList = dto.getItemImgDtoList().stream()
                    .map(ItemImg::toEntity)    // DTO → 엔티티
                    .collect(Collectors.toList());

            // 생성된 엔티티를 Item에 추가
            for (ItemImg itemImg : itemImgList) {
                item.addItemImg(itemImg);     // 연관관계 주인 설정, 컬렉션에 추가
            }
        }

        // 5. DB에 저장
        return itemRepository.save(item);
    }

    // 상품정보 가져오기
    @Transactional(readOnly = true)
    public ItemCreateRequest getItemDetail(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        return ItemCreateRequest.toDTO(item);

    }

    // 상품 수정
    @Transactional
    public void updateItem(ItemCreateRequest itemFormDto, List<MultipartFile> images) {
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(ItemNotFoundException::new);
        item.updateItem(itemFormDto); //기존 필드 변경

        item.getOptions().clear(); //상품 옵션 재설정
        List<ItemOption> itemOptionList = itemFormDto.getOptions().stream()
            .map(ItemOption::toEntity)    // DTO → 엔티티
            .collect(Collectors.toList());
        for (ItemOption option : itemOptionList) {
            item.addOption(option);     // 연관관계 주인 설정, 컬렉션에 추가
        }


        item.getItemImgs().clear();//상품 이미지 재설정
        List<ItemImgDTO> itemImgDtoList = itemImgService.saveItemImgList(images); //새로운 이미지 저장
        for (ItemImgDTO imgDto : itemImgDtoList) { //이미지 - 상품 매핑
            ItemImg itemImg = ItemImg.toEntity(imgDto);
            item.addItemImg(itemImg);
        }
    }

    @Transactional
    //전체 상품 페이지 정렬 - 상품 리스트 불러오기 (날짜순 정렬) jh
    public Page<Item> getListByCreateDate(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10); //조회할 페이지 수
        return this.itemRepository.findAll(pageable);
    }
}
