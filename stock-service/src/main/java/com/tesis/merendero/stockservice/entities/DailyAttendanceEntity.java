package com.tesis.merendero.stockservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "daily_attendances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyAttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merendero_id")
    private Long merenderoId;

    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate;

    @Column(name = "people_served")
    private Integer peopleServed;
}
