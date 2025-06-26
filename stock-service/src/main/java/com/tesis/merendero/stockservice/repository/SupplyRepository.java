package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {

    // Obtener todos los supplies globales
    @Query("SELECT s FROM SupplyEntity s WHERE s.merenderoId IS NULL")
    List<SupplyEntity> findAllGlobalSupplies();

    List<SupplyEntity> findAllById(Iterable<Long> ids);

    List<SupplyEntity> findByMerenderoIdAndActiveTrue(Long merenderoId);
}
