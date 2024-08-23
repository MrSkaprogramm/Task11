package com.andersen.tr.service;

import com.andersen.tr.model.Person;
import com.andersen.tr.dao.DaoException;

public interface PersonServiceInterface {

    public void savePerson() throws DaoException;

    public Person getPerson() throws DaoException;

    public void deletePerson(Person person);

    public void updatePersonAndCreateCar(Person person);
}
