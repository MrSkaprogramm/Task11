package com.andersen.tr.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketData {
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
