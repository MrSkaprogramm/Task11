package com.andersen.tr.service.impl;

import com.andersen.tr.model.TicketData;
import com.andersen.tr.model.TicketType;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.TicketDao;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import com.andersen.tr.service.TicketServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Service
public class TicketService implements TicketServiceInterface {
    static Scanner scanner = new Scanner(System.in);

    private final TicketDao ticketDao;

    @Autowired
    public TicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public Ticket createTicket(User user) {
        System.out.println("Enter ticket type:");
        String typeString = scanner.nextLine();
        TicketType ticketType = TicketType.valueOf(typeString.toUpperCase());
        System.out.println("Enter ticket class:");
        String ticketClassString = scanner.nextLine();
        LocalDate creationDate = detectTicketTime();

        Ticket ticket = new Ticket();
        ticket.setTicketClass(ticketClassString);
        ticket.setTicketType(ticketType);
        ticket.setStartDate(creationDate);
        ticket.setUser(user);

        return ticket;
    }

    @Override
    public Ticket saveTicket(User user) {
        Ticket ticket = createTicket(user);

        try {
            ticketDao.saveTicket(ticket);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Ticket created");

        return ticket;
    }

    @Transactional
    @Override
    public void deleteTicket(User user) {
        System.out.println("Which ticket you want to delete?");
        int ticketNum = scanner.nextInt();
        scanner.nextLine();
        try {
            ticketDao.deleteTicket(user.getTickets().get(ticketNum));
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void showTicket(User user) {
        System.out.println("Enter number of ticket:");
        int ticketNum = scanner.nextInt();
        scanner.nextLine();

        Ticket ticket = null;
        try {
            if (!ticketDao.checkTicketExist(ticketNum, user.getId())) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                ticket = ticketDao.fetchTicketById(user.getTickets().get(ticketNum).getId(), user);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Ticket with " + "ticket id " + ticket.getId() + "\n" + "- userId: "
                + ticket.getUser().getId() + "\n" + "- ticketType: " + ticket.getTicketType() + "\n" + "- creationDate: "
                + ticket.getStartDate() + "\n" + "- ticketClass: "
                + ticket.getTicketClass());
    }

    @Transactional
    @Override
    public void updateTicket(User user) {
        System.out.println("Enter number of ticket:");
        int ticketNum = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter ticket class:");
        String ticketClassString = scanner.nextLine().toUpperCase();

        System.out.println("Enter type of ticket:");
        String type = scanner.nextLine();
        TicketType ticketType = TicketType.valueOf(type.toUpperCase());

        try {
            if (!(ticketDao.checkTicketExist(ticketNum, user.getId()))) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                Ticket ticket = user.getTickets().get(ticketNum);
                ticket.setTicketType(ticketType);
                ticket.setTicketClass(ticketClassString);
                ticketDao.updateTicket(ticket);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public LocalDate detectTicketTime(){
        LocalDate creationDate = LocalDate.now();
        return creationDate;
    }

    @Override
    public void extractTicketData() {
        String jsonFilePath = "classpath:ticketData.txt";
        List<TicketData> ticketDataList = null;
        try {
            ticketDataList = ticketDao.extractTicketData(jsonFilePath);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }

        for (TicketData ticketData : ticketDataList) {
            System.out.println("Ticket Class: " + ticketData.getTicketClass());
            System.out.println("Ticket Type: " + ticketData.getTicketType());
            System.out.println("Start Date: " + ticketData.getStartDate());
            System.out.println("Price: " + ticketData.getPrice());
            System.out.println();
        }
    }
}