package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.SupplyCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyCategoryRepository extends JpaRepository<SupplyCategoryEntity, Long> {
}
