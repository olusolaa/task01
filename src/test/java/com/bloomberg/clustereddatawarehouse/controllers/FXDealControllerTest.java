package com.bloomberg.clustereddatawarehouse.controllers;

import com.bloomberg.clustereddatawarehouse.dtos.FXDealDto;
import com.bloomberg.clustereddatawarehouse.exceptionhandler.GlobalExceptionHandler;
import com.bloomberg.clustereddatawarehouse.services.FXDealService;
import com.bloomberg.clustereddatawarehouse.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FXDealControllerTest {

    private MockMvc mockMvc;
    private  ObjectMapper objectMapper;

    @Mock
    private FXDealService fxDealService;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FXDealController(fxDealService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testCreateFXDeal() throws Exception {
        FXDealDto fxDealDto = createFXDealDto();

        ApiResponse<Object> expectedResponse = ApiResponse.builder()
        .message("FX deal added successfully")
        .status(HttpStatus.CREATED)
        .data(fxDealDto)
                .build();

        when(fxDealService.saveFXDeal(any(FXDealDto.class))).thenReturn(fxDealDto);

        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fxDealDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(fxDealService, times(1)).saveFXDeal(any(FXDealDto.class));
    }

    @Test
    public void testCreateFXDealValidationFailure() throws Exception {
        FXDealDto fxDealDto = FXDealDto.builder()
                .uniqueId("")
                .amount(new BigDecimal("-1000"))
                .fromCurrency("USDX")
                .toCurrency("EUR")
                .timestamp(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/v1/fx-deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fxDealDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("FX deal unique ID is required")))
                .andExpect(content().string(containsString("Amount is required and must be greater than zero")))
                .andExpect(content().string(containsString("Ordering currency is required")));

        verify(fxDealService, never()).saveFXDeal(any(FXDealDto.class));

    }

    @Test
    public void testGetAllFXDeals() throws Exception {
        FXDealDto fxDealDto1 = createFXDealDto();
        FXDealDto fxDealDto2 = createFXDealDto();

        List<FXDealDto> fxDealDtos = Arrays.asList(fxDealDto1, fxDealDto2);
        Page<FXDealDto> fxDeals = new PageImpl<>(fxDealDtos, PageRequest.of(0, 10), fxDealDtos.size());

        when(fxDealService.listFXDeals(anyInt(), anyInt())).thenReturn(fxDeals);

        mockMvc.perform(get("/api/v1/fx-deals")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.content.length()").value(fxDealDtos.size()));

        verify(fxDealService, times(1)).listFXDeals(anyInt(), anyInt());
    }

    @Test
    public void testGetFXDealById() throws Exception {
        FXDealDto fxDealDto = createFXDealDto();
        String dealId = fxDealDto.getUniqueId();

        ApiResponse<Object> expectedResponse = ApiResponse.builder()
                .message("FX deal retrieved successfully")
                .status(HttpStatus.OK)
                .data(fxDealDto)
                .build();

        when(fxDealService.getFXDeal(eq(dealId))).thenReturn(fxDealDto);

        mockMvc.perform(get("/api/v1/fx-deals/{dealId}", dealId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(fxDealService, times(1)).getFXDeal(eq(dealId));
    }

    private FXDealDto createFXDealDto() {
        return FXDealDto.builder()
                .uniqueId(UUID.randomUUID().toString())
                .amount(new BigDecimal("1000"))
                .fromCurrency("USD")
                .toCurrency("EUR")
                .timestamp(LocalDateTime.now())
                .build();
    }

}
