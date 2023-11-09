package com.bloomberg.clustereddatawarehouse.dtos;

import com.bloomberg.clustereddatawarehouse.exceptions.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonFormat()
public class FXDealDto {

    @JsonProperty("unique_id")
    private String uniqueId;

    private BigDecimal amount;

    @JsonProperty("from_currency")
    private String fromCurrency;

    @JsonProperty("to_currency")
    private String toCurrency;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    public void validate() {
        List<String> errors = new ArrayList<>();

        ensureNotNull(uniqueId, "FX deal unique ID is required", errors);
        ensureNotNull(amount, "Amount is required and must be greater than zero", errors);
        ensureValidCurrency(fromCurrency, "Ordering currency is required", errors);
        ensureValidCurrency(toCurrency, "Destination currency is required", errors);
        ensureNotNull(timestamp, "FX deal timestamp is required", errors);
        ensureDifferentCurrencies(fromCurrency, toCurrency, errors);

        if (!errors.isEmpty()) {
            throw new InvalidRequestException(String.join(", ", errors));
        }
    }

    private void ensureNotNull(Object value, String errorMessage, List<String> errors) {
        if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
            errors.add(errorMessage);
        } else if (value instanceof BigDecimal && ((BigDecimal) value).compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(errorMessage);
        }
    }

    private void ensureValidCurrency(String currency, String errorMessage, List<String> errors) {
        if (currency == null || !isValidCurrencyCode(currency)) {
            errors.add(errorMessage);
        }
    }

    private void ensureDifferentCurrencies(String fromCurrency, String toCurrency, List<String> errors) {
        if (fromCurrency != null && fromCurrency.equals(toCurrency)) {
            errors.add("From and to currencies should not be the same");
        }
    }

    private boolean isValidCurrencyCode(String currencyCode) {
        return Currency.getAvailableCurrencies().stream()
                .anyMatch(currency -> currency.getCurrencyCode().equalsIgnoreCase(currencyCode));
    }
}
