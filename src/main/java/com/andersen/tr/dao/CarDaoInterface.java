package com.andersen.tr.dao;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.TicketData;
import com.andersen.tr.model.Person;

import java.util.List;

public interface CarDaoInterface {

    public void saveCar(Car car) throws DaoException;

    public Car fetchCarById(int id, Person person) throws DaoException;

    public void updateCar(Car car) throws DaoException;

    public void deleteCar(Car car) throws DaoException;

    public boolean checkCarExist(int ticketNum, int userId) throws DaoException;

    public List<TicketData> extractTicketData(String jsonFilePath) throws DaoException;
}
