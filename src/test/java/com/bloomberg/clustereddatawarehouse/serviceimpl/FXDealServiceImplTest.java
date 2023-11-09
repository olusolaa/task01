package com.bloomberg.clustereddatawarehouse.serviceimpl;

import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.exceptions.DuplicateFXDealException;
import com.bloomberg.clustereddatawarehouse.exceptions.NotFoundException;
import com.bloomberg.clustereddatawarehouse.models.FXDeal;
import com.bloomberg.clustereddatawarehouse.repositories.FXDealRepository;
import com.bloomberg.clustereddatawarehouse.services.serviceimpl.FXDealServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FXDealServiceImplTest {

    @Mock
    private FXDealRepository fxDealRepository;

    @InjectMocks
    private FXDealServiceImpl fxDealService;

    private FXDealDto fxDealDto;
    private FXDeal fxDeal;

    @BeforeEach
    void setUp() {
        fxDealDto = new FXDealDto(
                "unique-id",
                new BigDecimal("1000"),
                "USD",
                "EUR",
                LocalDateTime.now()
        );

        fxDeal = FXDeal.builder()
                .uniqueId("unique-id")
                .amount(new BigDecimal("1000"))
                .fromCurrency(Currency.getInstance("USD"))
                .toCurrency(Currency.getInstance("EUR"))
                .timestamp(LocalDateTime.now()).build();
    }

    @Test
    void whenSaveFXDealWithUniqueID_thenThrowDuplicateFXDealException() {
        when(fxDealRepository.findByUniqueId(fxDealDto.getUniqueId())).thenReturn(Optional.of(fxDeal));
        assertThrows(DuplicateFXDealException.class, () -> fxDealService.saveFXDeal(fxDealDto));
        verify(fxDealRepository, never()).save(any(FXDeal.class));
    }

    @Test
    void whenSaveFXDealWithNewUniqueID_thenSaveSuccessfully() {
        when(fxDealRepository.findByUniqueId(fxDealDto.getUniqueId())).thenReturn(Optional.empty());
        when(fxDealRepository.save(any(FXDeal.class))).thenReturn(fxDeal);

        FXDealDto savedFxDealDto = fxDealService.saveFXDeal(fxDealDto);

        assertNotNull(savedFxDealDto);
        assertEquals(fxDealDto.getUniqueId(), savedFxDealDto.getUniqueId());

        verify(fxDealRepository).findByUniqueId(fxDealDto.getUniqueId());
        verify(fxDealRepository).save(any(FXDeal.class));
    }

    @Test
    void whenGetFXDealWithValidID_thenRetrieveDeal() {
        when(fxDealRepository.findByUniqueId(fxDealDto.getUniqueId())).thenReturn(Optional.of(fxDeal));

        FXDealDto retrievedDeal = fxDealService.getFXDeal(fxDealDto.getUniqueId());

        assertNotNull(retrievedDeal);
        assertEquals(fxDealDto.getUniqueId(), retrievedDeal.getUniqueId());

        verify(fxDealRepository).findByUniqueId(fxDealDto.getUniqueId());
    }

    @Test
    void whenGetFXDealWithInvalidID_thenThrowNotFoundException() {
        when(fxDealRepository.findByUniqueId("invalid-id")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> fxDealService.getFXDeal("invalid-id"));

        verify(fxDealRepository).findByUniqueId("invalid-id");
    }

    @Test
    void whenListFXDeals_thenRetrieveDealsPage() {
        Page<FXDeal> fxDealPage = new PageImpl<>(Collections.singletonList(fxDeal));
        when(fxDealRepository.findAll(any(PageRequest.class))).thenReturn(fxDealPage);

        Page<FXDealDto> retrievedPage = fxDealService.listFXDeals(0, 1);

        assertNotNull(retrievedPage);
        assertFalse(retrievedPage.isEmpty());

        verify(fxDealRepository).findAll(any(PageRequest.class));
    }
}
