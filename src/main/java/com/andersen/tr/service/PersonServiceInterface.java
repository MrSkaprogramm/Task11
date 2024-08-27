package com.andersen.tr.service;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.repository.DaoException;

public interface PersonServiceInterface {

    public Person savePerson() throws DaoException;

    public Person getPerson() throws DaoException;

    public String deletePerson(Person person);

    public String updatePersonAndCreateCar(Person person);

    public Car getSingleCar();
}
