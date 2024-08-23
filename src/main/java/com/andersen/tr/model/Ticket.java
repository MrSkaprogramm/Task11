package com.andersen.tr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "\"Ticket\"", schema = "public")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "ticket_class", nullable = false)
    private String ticketClass;
    @Column(name = "ticket_type", nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TicketType ticketType;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Ticket() {}

    public Ticket(String ticketClass, TicketType ticketType, LocalDate startDate, BigDecimal price, User user) {
        this.ticketClass = ticketClass;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.user = user;
    }

    public Ticket(int id, String ticketClass, TicketType ticketType, LocalDate startDate, User user) {
        this.id = id;
        this.ticketClass = ticketClass;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.user = user;
    }
}
