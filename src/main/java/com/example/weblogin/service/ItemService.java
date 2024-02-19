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
import org.springframework.web.multipart.MultipartFile;

import com.example.weblogin.config.Exception.ServiceUtils;
import com.example.weblogin.domain.DTO.ItemFormDto;
import com.example.weblogin.domain.DTO.ItemImgDto;
import com.example.weblogin.domain.ItemImg.ItemImg;
import com.example.weblogin.domain.ItemImg.ItemImgRepository;
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
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;

	private final OrderItemRepository orderItemRepository;

	private final SaleItemRepository saleItemRepository;
	private final CategorieRepository categorieRepository;
	private final BrandRepository brandRepository;

	// 상품 등록
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		// 상품 등록
		Categorie category = categorieRepository.findKategorieById(itemFormDto.getCategory());
		Brand brand = brandRepository.findBrandById(itemFormDto.getBrand());
		Item item = itemFormDto.toEntity(brand, category);
		itemRepository.save(item);

		//이미지 등록
		for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
			ItemImg itemImg = ItemImg.builder()
				.item(item)
				.repimgYn(i == 0 ? "Y" : "N")
				.build();

			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}

		return item.getId();
	}

	@Transactional(readOnly = true)
	public ItemFormDto getItemDetail(Long itemId) {

		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();

		for (ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}

		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);

		return itemFormDto;
	}

	// 상품 수정
	@Transactional
	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

		//상품 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
		item.updateItem(itemFormDto);

		List<Long> itemImgIds = itemFormDto.getItemImgIds();

		//이미지 등록
		for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}

		return item.getId();
	}

	//상품 삭제
	@Transactional
	public void deleteItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new ServiceUtils(String.valueOf(itemId)));
		if (item.getStockNumber() > 0) {
			throw new ServiceUtils("cannot delete item because it is still in stock");
		}
		List<OrderItem> orderItems = orderItemRepository.findByItemId(itemId);
		if (!orderItems.isEmpty()) {
			throw new ServiceUtils("cannot delete item because it is included in orders");
		}
		List<SaleItem> saleItems = saleItemRepository.findByItem(itemId);
		if (!saleItems.isEmpty()) {
			throw new ServiceUtils("cannot delete item because it is included in sales");
		}
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
