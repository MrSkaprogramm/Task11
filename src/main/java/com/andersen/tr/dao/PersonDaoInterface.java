package com.andersen.tr.dao;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;

public interface PersonDaoInterface {

    public void savePerson(Person person) throws DaoException;

    public Person getPersonById(int id) throws DaoException;

    public void deletePerson(Person person) throws DaoException;

    public boolean checkPersonExist(int userId) throws DaoException;

    public void updatePersonAndCar(Person person, Car car) throws DaoException;

    public void updatePerson(Person person) throws DaoException;
}
