package com.example.weblogin.service;

import com.example.weblogin.config.Exception.ItemNotFoundException;
import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.item.ItemSellStatus;
import com.example.weblogin.domain.member.Member;
import com.example.weblogin.domain.sale.Sale;
import com.example.weblogin.domain.sale.SaleRepository;
import com.example.weblogin.domain.saleitem.SaleItem;
import com.example.weblogin.domain.saleitem.SaleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private static final IllegalStateException IllegalStateException = new IllegalStateException("존재하지 않는 상품입니다.");
    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;
    private final SaleItemRepository saleItemRepository;

    /**
     * 판매내역 전체 조회
     */
    public List<SaleInfo> getSaleInfoList() {
        List<Sale> sales = saleRepository.findAllByOrderByIdDesc();
        return sales.stream().map(sale -> new SaleInfo(sale.getSaleDate(), sale.getTotalProfit())).collect(Collectors.toList());
    }

    /**
     * 판매 중인 상품 조회
     */
    public List<Item> getItemsOnSaller(Member Member) {
        return itemRepository.findByAdmin(Member);
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

        if (item.getItemSellStatus() == ItemSellStatus.NOT_SALE || item.getItemSellStatus() == ItemSellStatus.SOLD_OUT) {
            item.updateSellStatus(ItemSellStatus.SELL);
        } else {
            throw new IllegalArgumentException("이미 판매중인 상품입니다.");
        }

    }

    /**
     * 상품 판매 내역 조회
     */
    public List<SaleItem> getSaleItems(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        return saleItemRepository.findAllByItem(item);
    }


    /**
     * 상품별 수익 조회
     */
    public Long getProfitByItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        List<SaleItem> saleItems = saleItemRepository.findAllByItem(item);

        return saleItems.stream().mapToLong(SaleItem::getProfit).sum();
    }
}
