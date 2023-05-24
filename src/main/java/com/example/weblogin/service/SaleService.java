package com.example.weblogin.service;

import com.example.weblogin.domain.DTO.SaleInfo;
import com.example.weblogin.domain.item.Item;
import com.example.weblogin.domain.item.ItemRepository;
import com.example.weblogin.domain.item.ItemSellStatus;
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

    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;

    private final SaleItemRepository saleItemRepository;

    private static final IllegalStateException IllegalStateException =  new IllegalStateException("존재하지 않는 상품입니다.");
    /**
     * 판매내역 전체 조회
     */
    public List<SaleInfo> getSaleInfoList() {
        List<Sale> sales = saleRepository.findAllByOrderByIdDesc();
        return sales.stream()
                .map(sale -> new SaleInfo(sale.getSaleDate(), sale.getTotalProfit()))
                .collect(Collectors.toList());
    }

    /**
     * 판매 중인 상품 조회
     */
    public List<Item> getItemsOnSale() {
        return itemRepository.findAllByitemSellStatus(ItemSellStatus.SELL);
    }

    /**
     * 상품 판매 중지
     */
    @Transactional
    public void stopSellingItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        if (item.getItemSellStatus() == ItemSellStatus.NOT_SALE || item.getItemSellStatus() == ItemSellStatus.SOLD_OUT) {
            throw new IllegalStateException("판매할 수 없는 상품입니다.");
        }

        item.stopSelling();
    }

    /**
     * 상품 판매 재개
     */
    @Transactional
    public void resumeSellingItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        if (item.getItemSellStatus() != ItemSellStatus.NOT_SALE) {
            throw new IllegalStateException("판매를 재개할 수 없는 상품입니다.");
        }

        item.resumeSelling();
    }

    /**
     * 상품 판매 내역 조회
     */
    public List<SaleItem> getSaleItems(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        return saleItemRepository.findAllByItem(item);
    }



    /**
     * 상품별 수익 조회
     */
    public Long getProfitByItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));

        List<SaleItem> saleItems = saleItemRepository.findAllByItem(item);

        return saleItems.stream().mapToLong(SaleItem::getProfit).sum();
    }
}
