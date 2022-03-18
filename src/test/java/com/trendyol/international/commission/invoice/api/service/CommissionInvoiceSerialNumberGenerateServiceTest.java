package com.trendyol.international.commission.invoice.api.service;

import com.trendyol.international.commission.invoice.api.domain.CommissionInvoiceNumberSequence;
import com.trendyol.international.commission.invoice.api.repository.CommissionInvoiceNumberSequenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceSerialNumberGenerateServiceTest {

    @InjectMocks
    private CommissionInvoiceSerialNumberGenerateService commissionInvoiceSerialNumberGenerateService;

    @Mock
    private CommissionInvoiceNumberSequenceRepository commissionInvoiceNumberSequenceRepository;

    // NULL -> 1 - CREATE - TBV-2022-000000001
    @Test
    public void it_should_return_serial_number_with_ending_zero_when_latest_sequence_not_exist() {
        //given
        CommissionInvoiceNumberSequence commissionInvoiceNumberSequence = CommissionInvoiceNumberSequence.builder()
                .serialKey("TBV")
                .invoiceYear(2022)
                .latestSequence(0L)
                .build();

        when(commissionInvoiceNumberSequenceRepository.findBySerialKeyAndInvoiceYear("TBV", 2022)).thenReturn(Optional.empty());
        when(commissionInvoiceNumberSequenceRepository.save(commissionInvoiceNumberSequence)).thenReturn(commissionInvoiceNumberSequence);

        //when
        String serialNumber = commissionInvoiceSerialNumberGenerateService.generate(2022);

        //then
        verify(commissionInvoiceNumberSequenceRepository, times(2)).save(any());
        assertThat(serialNumber).isEqualTo("TBV2022000000001");
    }

    // 1 -> 2    - CREATE - TBV-2022-000000002
    @Test
    public void it_should_return_serial_number_when_latest_sequence_exist() {
        //given
        CommissionInvoiceNumberSequence commissionInvoiceNumberSequence = CommissionInvoiceNumberSequence.builder()
                .serialKey("TBV")
                .invoiceYear(2022)
                .latestSequence(1L)
                .build();

        when(commissionInvoiceNumberSequenceRepository.findBySerialKeyAndInvoiceYear("TBV", 2022)).thenReturn(Optional.of(commissionInvoiceNumberSequence));

        //when
        String serialNumber = commissionInvoiceSerialNumberGenerateService.generate(2022);

        //then
        verify(commissionInvoiceNumberSequenceRepository).save(any());
        assertThat(serialNumber).isEqualTo("TBV2022000000002");
    }
}