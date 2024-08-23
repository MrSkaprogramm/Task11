package com.andersen.tr.dao;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.TicketData;
import com.andersen.tr.model.User;

import java.util.List;

public interface TicketDaoInterface {

    public void saveTicket(Ticket ticket) throws DaoException;

    public Ticket fetchTicketById(int id, User user) throws DaoException;

    public void updateTicket(Ticket ticket) throws DaoException;

    public void deleteTicket(Ticket ticket) throws DaoException;

    public boolean checkTicketExist(int ticketNum, int userId) throws DaoException;

    public List<TicketData> extractTicketData(String jsonFilePath) throws DaoException;
}
