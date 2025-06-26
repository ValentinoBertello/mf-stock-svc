package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.MerenderoSupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerenderoSupplyRepository extends JpaRepository<MerenderoSupplyEntity,
        MerenderoSupplyEntity.MerenderoSupplyId> {

    // Obtener todos los supplies asociados a un merendero
    @Query("SELECT m.id.supplyId FROM MerenderoSupplyEntity m WHERE m.id.merenderoId = :merenderoId")
    List<Long> findSupplyIdsByMerenderoId(@Param("merenderoId") Long merenderoId);

    // Verificar si existe una asociación
    boolean existsById_MerenderoIdAndId_SupplyId(Long merenderoId, Long supplyId);
}
