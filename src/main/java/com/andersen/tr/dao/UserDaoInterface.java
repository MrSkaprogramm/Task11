package com.andersen.tr.dao;

import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;

public interface UserDaoInterface {

    public void saveUser(User user) throws DaoException;

    public User getUserById(int id) throws DaoException;

    public void deleteUser(User user) throws DaoException;

    public boolean checkUserExist(int userId) throws DaoException;

    public void updateUserAndTickets(User user, Ticket ticket) throws DaoException;

    public void updateUser(User user) throws DaoException;
}
