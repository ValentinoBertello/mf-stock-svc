package com.tesis.merendero.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supply_outputs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyOutputEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merendero_id")
    private Long merenderoId;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private SupplyEntity supply;

    private BigDecimal quantity;

    @Column(name = "output_date")
    private LocalDateTime outputDate;

    @PrePersist
    public void beforePersist() {
        outputDate = LocalDateTime.now();
    }
}
