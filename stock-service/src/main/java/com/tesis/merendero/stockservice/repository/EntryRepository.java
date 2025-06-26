package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.SupplyEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<SupplyEntryEntity, Long> {

    List<SupplyEntryEntity> findByMerenderoId(Long merenderoId);

    //Consulta personalizada con JPQL
    @Query("""
        SELECT e FROM SupplyEntryEntity e
        WHERE
        e.entryDate BETWEEN :desde AND :hasta
        AND e.supply.id = :supplyId
        AND e.merenderoId = :merenderoId
        """)
    List<SupplyEntryEntity> findByDatesAndSupply(
            @Param("desde") LocalDateTime desde,
            @Param("hasta")LocalDateTime hasta,
            @Param("supplyId") Long supplyId,
            @Param("merenderoId") Long merenderoId
            );

//    @Query("""
//  SELECT e
//  FROM SupplyEntryEntity e
//  WHERE e.merenderoId = :merenderoId
//    AND e.entryDate  >= :startDateTime
//    AND e.entryDate  <= :endDateTime
//  ORDER BY e.entryDate ASC
//""")
//    List<SupplyEntryEntity> findAllByDates(@Param("desde") LocalDateTime desde,
//                                           @Param("hasta")LocalDateTime hasta,
//                                           @Param("merenderoId") Long merenderoId);
}
