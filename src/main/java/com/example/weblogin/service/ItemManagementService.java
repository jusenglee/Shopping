package com.example.weblogin.service;

import com.example.weblogin.domain.DTO.ItemCreateRequest;
import com.example.weblogin.domain.itemImg.ItemImg;
import com.example.weblogin.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 상품 저장과 이미지 관리 등을 하는 중앙 조정 서비스
 * 해당 서비스는 타 서비스간의 순환참조를 예방
 */
@Service
public class ItemManagementService {

    private final MemberService memberService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;

    public ItemManagementService(MemberService memberService, ItemService itemService, ItemImgService itemImgService) {
        this.memberService = memberService;
        this.itemService = itemService;
        this.itemImgService = itemImgService;
    }

    @Transactional
    public Long saveItemWithImages(ItemCreateRequest itemFormDto, List<MultipartFile> images) {
        // 로그인 유저(Member) 셋팅
        Member member = memberService.getCurrentUserMember();
        itemFormDto.setAdmin(member);

        // 아이템 저장
        Long itemId = itemService.saveItem(itemFormDto);
        if (itemId == null) {
            throw new IllegalStateException("아이템 저장 실패");
        }

        // 이미지 파일 저장 및 매핑
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                ItemImg itemImg = itemImgService.saveItemImg(image); // 이미지 저장
                itemImgService.addImageToItem(itemId, itemImg); // 이미지 매핑
            }
        }

        return itemId;
    }
}
