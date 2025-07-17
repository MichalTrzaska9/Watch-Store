package com.example.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    ORDER_IN_PROCESSING(1, "Zamówienie w realizacji"), ORDER_PACKED(2, "Zamówienie spakowane"),
    ORDER_IN_DELIVERY(3, "Zamówienie w doręczeniu"), ORDER_DELIVERED(4, "Zamówienie dostarczone"),
    ORDER_CANCELLED(5, "Zamówienie anulowane");

    private Integer id;
    private String name;
}
