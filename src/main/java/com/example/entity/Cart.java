package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserDetails user;

    @ManyToOne
    private Watch watch;

    private Integer quantity;

    @Transient
    private BigDecimal amount;

    @Transient
    private BigDecimal totalOrderAmount;

}
