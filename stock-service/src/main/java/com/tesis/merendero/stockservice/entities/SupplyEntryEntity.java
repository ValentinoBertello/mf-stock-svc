package com.tesis.merendero.stockservice.entities;

import com.tesis.merendero.stockservice.enums.EntryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supply_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merendero_id")
    private Long merenderoId;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private SupplyEntity supply;

    private BigDecimal quantity;

    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    @Enumerated(EnumType.STRING)  // Mapea el enum como String
    @Column(name = "entry_type")
    private EntryType entryType;

    @PrePersist
    public void beforePersist() {
        entryDate = LocalDateTime.now();
    }
}
