package com.bloomberg.clustereddatawarehouse.controllers;

import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.services.FXDealService;
import com.bloomberg.clustereddatawarehouse.util.ApiResponse;
import com.bloomberg.clustereddatawarehouse.util.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fx-deals")
@Slf4j
public class FXDealController {

    private final FXDealService fxDealService;

    public FXDealController(FXDealService fxDealService) {
        this.fxDealService = fxDealService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FXDealDto>> createFXDeal(@RequestBody FXDealDto request) {
        request.validate();
        log.info("Creating FX deal with unique ID: {}", request.getUniqueId());
        return ResponseBuilder.created(fxDealService.saveFXDeal(request), "FX deal added successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FXDealDto>>> getAllFXDeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseBuilder.ok(fxDealService.listFXDeals(page, size), "FX deals retrieved successfully");
    }

    @GetMapping("/{dealId}")
    public ResponseEntity<ApiResponse<FXDealDto>> getFXDealById(@PathVariable String dealId) {
        log.info("Retrieving FX deal with unique ID: {}", dealId);
        return ResponseBuilder.ok(fxDealService.getFXDeal(dealId), "FX deal retrieved successfully");
    }
}