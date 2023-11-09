package com.bloomberg.clustereddatawarehouse.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fx_deals")
public class FXDeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uniqueId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private Currency fromCurrency;
    @Column(nullable = false)
    private Currency toCurrency;
    @Column(nullable = false)
    private LocalDateTime timestamp;

}
