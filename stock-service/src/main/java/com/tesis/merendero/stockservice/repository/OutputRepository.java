package com.tesis.merendero.stockservice.repository;

import com.tesis.merendero.stockservice.entities.SupplyEntryEntity;
import com.tesis.merendero.stockservice.entities.SupplyOutputEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutputRepository extends JpaRepository<SupplyOutputEntity,Long> {

    List<SupplyOutputEntity> findByMerenderoId(Long merenderoId);

    //Consulta personalizada con JPQL
    @Query("""
        SELECT o FROM SupplyOutputEntity o
        WHERE
        o.outputDate BETWEEN :desde AND :hasta
        AND o.supply.id = :supplyId
        AND o.merenderoId = :merenderoId
        """)
    List<SupplyOutputEntity> findByDatesAndSupply(
            @Param("desde") LocalDateTime desde,
            @Param("hasta")LocalDateTime hasta,
            @Param("supplyId") Long supplyId,
            @Param("merenderoId") Long merenderoId
    );

//    @Query("""
//  SELECT e
//  FROM SupplyOutputEntity e
//  WHERE e.merenderoId = :merenderoId
//    AND e.outputDate  >= :startDateTime
//    AND e.outputDate  <= :endDateTime
//  ORDER BY e.outputDate ASC
//""")
//    List<SupplyOutputEntity> findAllByDates(@Param("desde") LocalDateTime desde,
//                                           @Param("hasta")LocalDateTime hasta,
//                                           @Param("merenderoId") Long merenderoId);
}
