package com.example.weblogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

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
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.itemCategory.Brand;
import com.example.weblogin.domain.itemCategory.BrandRepository;
import com.example.weblogin.domain.itemCategory.Categorie;
import com.example.weblogin.domain.itemCategory.CategorieRepository;
import com.example.weblogin.domain.orderItem.OrderItem;
import com.example.weblogin.domain.orderItem.OrderItemRepository;
import com.example.weblogin.domain.saleitem.SaleItem;
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

	// 상품 저장 후 ID 반환
	public Long saveItem(Item item) {
		Item savedItem = itemRepository.save(item);
		return savedItem.getId(); // 저장된 상품의 ID 반환
	}

	/**
	 * 상품 존재여부 확인
	 * @param itemId
	 */
	public void validateItemExists(Long itemId) {
		itemRepository.findById(itemId)
			.orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + itemId));
	}

	public Long saveItem(@ModelAttribute ItemFormDto itemFormDto) throws Exception {
		try {
			Categorie category = categorieRepository.findCategorieById(itemFormDto.getCategory())
				.orElseThrow(() -> new EntityNotFoundException("Category not found"));
			Brand brand = brandRepository.findBrandById(itemFormDto.getBrand())
				.orElseThrow(() -> new EntityNotFoundException("Brand not found"));

			Item item = new Item();
			item.setItemNm(itemFormDto.getItemNm());
			item.setItemDetail(itemFormDto.getItemDetail());
			item.setItemSellStatus(itemFormDto.getItemSellStatus());
			item.setPrice(itemFormDto.getPrice());
			item.setStockNumber(itemFormDto.getStockNumber());
			item.setCategory(category);
			item.setBrand(brand);
			item.setCountview(0); // 초기 조회수 0
			item.setHeart(0);
			item.setAdmin(itemFormDto.getAdmin());
			return saveItem(item);
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
			.orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다. ID: " + itemId));

		return ItemFormDto.builder()
			.id(item.getId())
			.category(item.getCategory().getId())
			.brand(item.getBrand().getId())
			.admin(item.getAdmin())
			.itemNm(item.getItemNm())
			.itemDetail(item.getItemDetail())
			.itemSellStatus(item.getItemSellStatus())
			.price(item.getPrice())
			.stockNumber(item.getStockNumber())
			.countview(item.getCountview())
			.heart(item.getHeart())
			.build();
	}

	// 상품 수정
	@Transactional
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

		//상품 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		item.updateItem(itemFormDto);

		//이미지 등록
		// for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
		// 	itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		// }

		return item.getId();
	}

	//상품 삭제
	@Transactional
	public void deleteItem(Long itemId) {
		// 상품 조회 및 예외 처리
		Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);

		// 재고 확인
		if (item.getStockNumber() > 0) {
			throw new DataNotFoundException("재고가 남아 있는 상품은 삭제할 수 없습니다. ID: " + itemId);
		}

		// 주문 항목 확인
		List<OrderItem> orderItems = orderItemRepository.findByItemId(itemId);
		if (!orderItems.isEmpty()) {
			throw new DataNotFoundException("이미 주문된 상품은 삭제할 수 없습니다. ID: " + itemId);
		}

		// 판매 항목 확인
		List<SaleItem> saleItems = saleItemRepository.findByItem(itemId);
		if (!saleItems.isEmpty()) {
			throw new DataNotFoundException("판매 항목에 등록된 상품은 삭제할 수 없습니다. ID: " + itemId);
		}

		// 상품 삭제
		itemRepository.delete(item);
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
