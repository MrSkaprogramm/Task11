package com.andersen.tr.repository;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.TicketData;

import java.util.List;

public interface CarDaoInterface {

    public void saveCar(Car car) throws DaoException;

    public Car fetchCarById(int id) throws DaoException;

    public void updateCar(Car car) throws DaoException;

    public void deleteCar(Car car) throws DaoException;

    public boolean checkCarExist(int carNum) throws DaoException;

    public List<TicketData> extractTicketData(String jsonFilePath) throws DaoException;
}
