package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByEntryIdIn(List<Long> entryIds);
    List<ExpenseEntity> findByMerenderoId(Long merenderoId);
}
