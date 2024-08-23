package com.andersen.tr.service;

import com.andersen.tr.model.User;
import com.andersen.tr.dao.DaoException;

public interface UserServiceInterface {

    public void saveUser() throws DaoException;

    public User getUser() throws DaoException;

    public void deleteUser(User user);

    public void updateUserAndCreateTicket(User user);
}
