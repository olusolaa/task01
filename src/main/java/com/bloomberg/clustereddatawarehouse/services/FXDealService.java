package com.bloomberg.clustereddatawarehouse.services;


import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.exceptions.InvalidRequestException;
import org.springframework.data.domain.Page;

public interface FXDealService {
    FXDealDto saveFXDeal(FXDealDto dealRequestDto) throws InvalidRequestException;
    FXDealDto getFXDeal(String dealId);
    Page<FXDealDto> listFXDeals(int page, int size);
}
