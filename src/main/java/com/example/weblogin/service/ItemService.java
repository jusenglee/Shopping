package com.example.weblogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.weblogin.config.Exception.DataNotFoundException;
import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.orderItem.OrderItemRepository;
import com.example.weblogin.domain.saleitem.SaleItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {

	private final ItemRepository itemRepository;

	private final OrderItemRepository orderItemRepository;

	private final SaleItemRepository saleItemRepository;
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;

	@Transactional
	public Long saveItem(@ModelAttribute ItemFormDto itemFormDto) {
		try {
			Categorie category = categorieRepository.findCategorieById(itemFormDto.getCategory())
				.orElseThrow(() -> new DataNotFoundException("카테고리 정보를 찾을 수 없습니다."));
			Brand brand = brandRepository.findBrandById(itemFormDto.getBrand())
				.orElseThrow(() -> new DataNotFoundException("브랜드 정보를 찾을 수 없습니다. "));

			Item item = new Item();
			item.setItemNm(itemFormDto.getItemNm());
			item.setItemDetail(itemFormDto.getItemDetail());
			item.setItemSellStatus(itemFormDto.getItemSellStatus());
			item.setPrice(itemFormDto.getPrice());
			item.setInventory(itemFormDto.getInventory());
			item.setCategory(category);
			item.setBrand(brand);
			item.setCountview(0); // 초기 조회수 0
			item.setHeart(0);
			item.setAdmin(itemFormDto.getAdmin());
			itemRepository.save(item);
			return item.getId();
		} catch (Exception e) {
			final Exception e1 = e;
			e1.printStackTrace();
		}
		return null;
	}

	// 상품정보 가져오기
	@Transactional(readOnly = true)
	public ItemFormDto getItemDetail(Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(ItemNotFoundException::new);
		return ItemFormDto.builder()
			.id(item.getId())
			.category(item.getCategory().getId())
			.brand(item.getBrand().getId())
			.admin(item.getAdmin())
			.itemNm(item.getItemNm())
			.itemDetail(item.getItemDetail())
			.itemSellStatus(item.getItemSellStatus())
			.price(item.getPrice())
			.inventory(item.getInventory())
			.itemImgDtoList(item.getItemImgs())
			.countview(item.getCountview())
			.heart(item.getHeart())
			.build();
	}

	// 상품 수정
	@Transactional
	public void updateItem(ItemFormDto itemFormDto) {
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(ItemNotFoundException::new);
		item.updateItem(itemFormDto);
	}

	@Transactional
	//좋아요 추가 jh
	public void heart1(Long id) {
		Item item = itemRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
		item.increaseHeart();
		itemRepository.save(item);
	}

	@Transactional
	//조회수 jh
	public Item getItemView(Long id) {
		Optional<Item> itemview = this.itemRepository.findById(id);

		if (itemview.isPresent()) {
			Item itemview1 = itemview.get();
			itemview1.setCountview(itemview1.getCountview() + 1);
			this.itemRepository.save(itemview1);
			return itemview1;
		} else {
			throw new IllegalArgumentException("question not found");
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
