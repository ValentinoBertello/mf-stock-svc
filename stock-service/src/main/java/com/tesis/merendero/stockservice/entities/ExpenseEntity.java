package com.tesis.merendero.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merendero_id")
    private Long merenderoId;

    @Column(name = "amount")
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "entry_id")
    private SupplyEntryEntity entry; // Nueva relación

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private SupplyEntity supply;

    @ManyToOne
    @JoinColumn(name = "type_expense_id")
    private TypeExpenseEntity type;

    @Column(name = "expense_date")
    private LocalDateTime expenseDate;
}
