package com.bloomberg.clustereddatawarehouse.services.serviceimpl;

import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.exceptions.*;
import com.bloomberg.clustereddatawarehouse.models.FXDeal;
import com.bloomberg.clustereddatawarehouse.repositories.FXDealRepository;
import com.bloomberg.clustereddatawarehouse.services.FXDealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Currency;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FXDealServiceImpl implements FXDealService {

    private final FXDealRepository fxDealRepository;

    public FXDealServiceImpl(FXDealRepository fxDealRepository) {
        this.fxDealRepository = fxDealRepository;
    }

    @Override
    public FXDealDto saveFXDeal(FXDealDto request) {
        log.debug("Attempting to save FX deal with unique ID: {}", request.getUniqueId());
        fxDealRepository.findByUniqueId(request.getUniqueId()).ifPresent(deal -> {
            String errorMessage = "FX deal already exists with ID: " + request.getUniqueId();
            log.error(errorMessage);
            throw new DuplicateFXDealException(errorMessage);
        });

        FXDeal deal = FXDeal.builder()
                .uniqueId(request.getUniqueId())
                .amount(request.getAmount())
                .fromCurrency(Currency.getInstance(request.getFromCurrency()))
                .toCurrency(Currency.getInstance(request.getToCurrency()))
                .timestamp(request.getTimestamp())
                .build();

        FXDeal savedDeal = fxDealRepository.save(deal);
        log.info("FX deal saved with unique ID: {}", savedDeal.getUniqueId());
        return mapToFXDealRequest(savedDeal);
    }

    @Override
    public FXDealDto getFXDeal(String dealId) {
        log.debug("Retrieving FX deal with unique ID: {}", dealId);
        FXDeal fxDeal = fxDealRepository.findByUniqueId(dealId)
                .orElseThrow(() -> new NotFoundException("FX Deal with ID " + dealId + " not found"));
        return mapToFXDealRequest(fxDeal);
    }

    @Override
    public Page<FXDealDto> listFXDeals(int page, int size) {
        log.debug("Listing FX deals - page: {}, size: {}", page, size);
        return fxDealRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToFXDealRequest);
    }

    private FXDealDto mapToFXDealRequest(FXDeal fxDeal) {
        return new FXDealDto(
                fxDeal.getUniqueId(),
                fxDeal.getAmount(),
                fxDeal.getFromCurrency().getCurrencyCode(),
                fxDeal.getToCurrency().getCurrencyCode(),
                fxDeal.getTimestamp()
        );
    }
}