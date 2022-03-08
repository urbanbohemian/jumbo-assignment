package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoice;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceServiceTest {

    @InjectMocks
    private CommissionInvoiceService commissionInvoiceService;

    @Mock
    private CommissionInvoiceRepository commissionInvoiceRepository;

    @Test
    public void it_should_save_commission_invoice() {
        //given
        CommissionInvoice commissionInvoice = new CommissionInvoice();

        //when
        commissionInvoiceService.saveCommissionInvoice(commissionInvoice);

        //then
        verify(commissionInvoiceRepository).save(commissionInvoice);
    }

    @Test
    public void it_should_() {

    }
}