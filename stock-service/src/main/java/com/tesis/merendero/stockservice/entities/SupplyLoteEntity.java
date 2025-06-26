package com.tesis.merendero.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "supply_lotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyLoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merendero_id")
    private Long merenderoId;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private SupplyEntity supply;

    @ManyToOne
    @JoinColumn(name = "entrada_id")
    private SupplyEntryEntity entry;

    @Column(name = "initial_quantity")
    private BigDecimal initialQuantity;

    @Column(name = "current_quantity")
    private BigDecimal currentQuantity;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    private boolean notified;
}
