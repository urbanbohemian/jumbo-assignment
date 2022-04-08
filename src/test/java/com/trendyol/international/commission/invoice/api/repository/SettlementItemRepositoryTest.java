package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.model.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;

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
    public void it_should_update_delivery_date_and_payment_date() {
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
        settlementItemRepository.save(settlementItem);

        //when
        Integer affected = settlementItemRepository.updateDeliveryDateAndPaymentDate(new Date(), new Date(), 1L);
        //then
        assertThat(affected).isGreaterThan(0);
    }
}