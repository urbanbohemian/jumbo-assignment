package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.SettlementItem;
import com.trendyol.international.commission.invoice.api.repository.SettlementItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SettlementItemServiceTest {

    @InjectMocks
    private SettlementItemService settlementItemService;

    @Mock
    private SettlementItemRepository settlementItemRepository;

    @Test
    public void it_should_save_settlement_item() {
        //given
        SettlementItem settlementItem = new SettlementItem();

        //when
        settlementItemService.saveSettlementItem(settlementItem);

        //then
        verify(settlementItemRepository).save(settlementItem);
    }
}
