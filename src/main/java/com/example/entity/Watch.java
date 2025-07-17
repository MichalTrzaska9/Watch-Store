package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String model;

    private String brand;

    @Column(length = 1000)
    private String description;

    private String condition;

    private BigDecimal price;

    private Integer stock;

    private String image;

    private Boolean isActive;

}
