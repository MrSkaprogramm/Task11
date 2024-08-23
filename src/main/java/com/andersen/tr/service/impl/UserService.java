package com.andersen.tr.service.impl;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.UserDao;
import com.andersen.tr.model.UserStatus;
import com.andersen.tr.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Service
public class UserService implements UserServiceInterface {
    static Scanner scanner = new Scanner(System.in);
    private final boolean isUpdateEnabled;
    private final TicketService ticketService;
    private final UserDao userDao;

    @Autowired
    public UserService(boolean isUpdateEnabled, TicketService ticketService, UserDao userDao) {
        this.isUpdateEnabled = isUpdateEnabled;
        this.ticketService = ticketService;
        this.userDao = userDao;
    }

    @Override
    public void saveUser() {
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        UserStatus userStatus = UserStatus.DEACTIVATED;
        User user = new User(name, userStatus);

        try {
            userDao.saveUser(user);
            System.out.println("User created");
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User getUser() {
        System.out.println("Enter user id:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        User user = null;
        try {
            if (!userDao.checkUserExist(userId)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                user = userDao.getUserById(userId);
            }
            System.out.println("User:" + "\n" + "- userId: " + user.getId() + "\n" + "- name: "
                    + user.getName() + "\n" + "- userStatus: " + user.getUserStatus());
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        return user;
    }

    @Transactional
    @Override
    public void deleteUser(User user) {
        try {
            if (!userDao.checkUserExist(user.getId())) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                userDao.deleteUser(user);
            }
        } catch (DaoException e) {
            System.err.println(e);
        }
    }

    @Transactional
    @Override
    public void updateUserAndCreateTicket(User user) {
        if (isUpdateEnabled) {
            try {
                user.setUserStatus(UserStatus.ACTIVATED);
                Ticket ticket = ticketService.createTicket(user);
                userDao.updateUserAndTickets(user, ticket);
            } catch (DaoException e) {
                System.err.println(e);
                e.printStackTrace();
                e.getCause();
            }

        } else {
            throw new IllegalArgumentException("You are not be able to do it!");
        }
    }
}
