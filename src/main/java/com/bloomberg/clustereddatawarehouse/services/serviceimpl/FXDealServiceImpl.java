package com.bloomberg.clustereddatawarehouse.services.serviceimpl;

import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.exceptions.InvalidRequestException;
import com.bloomberg.clustereddatawarehouse.repositories.FXDealRepository;
import com.bloomberg.clustereddatawarehouse.services.FXDealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FXDealServiceImpl implements FXDealService {

    private final FXDealRepository fxDealRepository;

    public FXDealServiceImpl(FXDealRepository fxDealRepository) {
        this.fxDealRepository = fxDealRepository;
    }

    @Override
    public FXDealDto saveFXDeal(FXDealDto dealRequestDto) throws InvalidRequestException {
        return null;
    }

    @Override
    public FXDealDto getFXDeal(String dealId) {
        return null;
    }

    @Override
    public Page<FXDealDto> listFXDeals(int page, int size) {
        return null;
    }
}