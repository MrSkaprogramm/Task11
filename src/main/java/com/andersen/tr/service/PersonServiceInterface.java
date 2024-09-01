package com.andersen.tr.service;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.PersonStatus;
import com.andersen.tr.repository.DaoException;

public interface PersonServiceInterface {

    public Person savePerson(String name, PersonStatus personStatus, String password) throws DaoException;

    public Person getPerson(int personId) throws DaoException;

    public String deletePerson(Person person);

    public String updatePersonAndCreateCar(Person person, CarType carType, String carBrandString);
}
