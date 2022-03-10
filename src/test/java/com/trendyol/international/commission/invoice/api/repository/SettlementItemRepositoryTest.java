package com.trendyol.international.commission.invoice.api.repository;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
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
                .sellerId(1L)
                .commissionAmount(BigDecimal.ONE)
                .deliveryDate(new Date())
                .paymentDate(new Date())
                .itemCreationDate(new Date())
                .build();

        //when
        SettlementItem savedSettlementItem = settlementItemRepository.save(settlementItem);

        //then
        assertThat(savedSettlementItem).isEqualTo(settlementItem);
    }
}