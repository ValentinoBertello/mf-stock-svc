package com.tesis.merendero.stockservice.entities;

import com.tesis.merendero.stockservice.enums.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "supplies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name = "min_quantity")
    private BigDecimal minQuantity;

    private Boolean active = true;

    @Column(name = "last_alert_date")
    private LocalDate lastAlertDate;

    @Column(name = "merendero_id", nullable = true)
    private Long merenderoId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SupplyCategoryEntity supplyCategory;
}
