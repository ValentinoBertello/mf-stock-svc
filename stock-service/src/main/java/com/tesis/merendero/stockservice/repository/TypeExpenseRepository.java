package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.TypeExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeExpenseRepository extends JpaRepository<TypeExpenseEntity, Long> {
    TypeExpenseEntity findByDescription(String description);
}
