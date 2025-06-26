package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.SupplyLoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SupplyLoteRepository extends JpaRepository<SupplyLoteEntity, Long> {

    List<SupplyLoteEntity> findByMerenderoIdAndSupplyId(Long merenderoId, Long supplyId);

    // Para obtener lotes con stock de un insumo específico
    List<SupplyLoteEntity> findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThanOrderByExpirationDateAsc(
            Long merenderoId,
            Long supplyId,
            BigDecimal currentQuantity
    );

    // Para inventario general de un merendero
    List<SupplyLoteEntity> findByMerenderoIdAndCurrentQuantityGreaterThan(Long merenderoId, BigDecimal currentQuantity);

    void deleteAllBySupplyId(Long supplyId);

    List<SupplyLoteEntity> findByMerenderoIdAndSupplyIdAndCurrentQuantityGreaterThan(
            Long merenderoId, Long supplyId, BigDecimal currentQuantity
    );
}
