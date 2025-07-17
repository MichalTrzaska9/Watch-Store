package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WatchOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderId;

    private LocalDateTime orderDate;

    @ManyToOne
    private Watch watch;

    private BigDecimal price;

    private Integer quantity;

    @ManyToOne
    private UserDetails userDetails;

    private String orderStatus;


    @OneToOne(cascade = CascadeType.ALL)
    private RecipientDetails recipientDetails;

    public String getFormattedOrderDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orderDate != null ? orderDate.format(formatter) : "";
    }
}
