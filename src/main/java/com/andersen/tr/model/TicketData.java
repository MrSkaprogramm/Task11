package com.andersen.tr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TicketData {
    @Id
    private int id;
    private String ticketClass;
    private String ticketType;
    private String startDate;
    private double price;

    public TicketData(String ticketClass, String ticketType, String startDate, double price) {
        this.ticketClass = ticketClass;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.price = price;
    }
}
