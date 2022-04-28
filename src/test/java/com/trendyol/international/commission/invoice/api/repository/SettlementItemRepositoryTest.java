package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.entity.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class SettlementItemRepositoryTest {

    @Autowired
    private SettlementItemRepository settlementItemRepository;

    @Test
    public void it_should_save_settlement_item() {
        //given
        SettlementItem settlementItem = SettlementItem.builder()
                .id(1L)
                .itemCreationDate(new Date())
                .sellerId(1L)
                .transactionType(TransactionType.SALE)
                .commissionAmount(BigDecimal.ONE)
                .storeFrontId(1L)
                .currency("EU")
                .build();

        //when
        SettlementItem savedSettlementItem = settlementItemRepository.save(settlementItem);

        //then
        assertThat(savedSettlementItem).isEqualTo(settlementItem);
    }

    @Test
    public void it_should_get_settlement_items() {
        //given
        SettlementItem settlementItem1 = SettlementItem.builder()
                .id(1L)
                .itemCreationDate(new Date(15L))
                .sellerId(1L)
                .transactionType(TransactionType.SALE)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date(25L))
                .storeFrontId(1L)
                .currency("EU")
                .build();
        SettlementItem settlementItem2 = SettlementItem.builder()
                .id(2L)
                .itemCreationDate(new Date(40L))
                .sellerId(1L)
                .transactionType(TransactionType.SALE)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date(65L))
                .storeFrontId(1L)
                .currency("EU")
                .build();
        SettlementItem settlementItem3 = SettlementItem.builder()
                .id(3L)
                .itemCreationDate(new Date(40L))
                .sellerId(1L)
                .transactionType(TransactionType.RETURN)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date(60L))
                .storeFrontId(1L)
                .currency("EU")
                .build();
        SettlementItem settlementItem4 = SettlementItem.builder()
                .id(4L)
                .itemCreationDate(new Date(15L))
                .sellerId(1L)
                .transactionType(TransactionType.RETURN)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date(25L))
                .storeFrontId(1L)
                .currency("EU")
                .build();
        SettlementItem settlementItem5 = SettlementItem.builder()
                .id(5L)
                .itemCreationDate(new Date(35L))
                .sellerId(2L)
                .transactionType(TransactionType.RETURN)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date(35L))
                .storeFrontId(1L)
                .currency("EU")
                .build();
        settlementItemRepository.saveAll(List.of(settlementItem1, settlementItem2, settlementItem3, settlementItem4, settlementItem5));

        //when
        List<SettlementItem> settlementItemList = settlementItemRepository.getSettlementItems(1L, new Date(20L), new Date(50L));

        //then
        assertThat(settlementItemList.stream().map(SettlementItem::getId)).containsExactly(1L, 3L);
    }
}