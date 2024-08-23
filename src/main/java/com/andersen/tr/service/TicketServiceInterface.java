package com.andersen.tr.service;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.model.User;

import java.time.LocalDate;
import java.util.List;

public interface TicketServiceInterface {

    public Ticket createTicket(User user);

    public Ticket saveTicket(User user);

    public void deleteTicket(User user);

    public void showTicket(User user);

    public void updateTicket(User user);

    public LocalDate detectTicketTime();

    public void extractTicketData();

}
