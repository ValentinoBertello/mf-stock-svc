package com.tesis.merendero.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "merendero_supplies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerenderoSupplyEntity {

    @EmbeddedId
    private MerenderoSupplyId id; // Clave compuesta

    // Clave compuesta (merendero_id + supply_id)
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MerenderoSupplyId implements Serializable {
        @Column(name = "merendero_id")
        private Long merenderoId; // Solo el ID, no una relación JPA

        @Column(name = "supply_id")
        private Long supplyId;
    }
}
