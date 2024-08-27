package com.andersen.tr.service.impl;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.PersonDao;
import com.andersen.tr.model.PersonStatus;
import com.andersen.tr.service.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;

@Service
public class PersonService implements PersonServiceInterface {
    static Scanner scanner = new Scanner(System.in);
    private final boolean isUpdateEnabled;
    private final CarService carService;
    private final PersonDao personDao;

    @Autowired
    public PersonService(boolean isUpdateEnabled, CarService carService, PersonDao personDao) {
        this.isUpdateEnabled = isUpdateEnabled;
        this.carService = carService;
        this.personDao = personDao;
    }

    @Override
    public void savePerson() {
        System.out.println("Enter person name:");
        String name = scanner.nextLine();
        PersonStatus personStatus = PersonStatus.DEACTIVATED;
        Person person = new Person(name, personStatus);

        try {
            personDao.savePerson(person);
            System.out.println("Person created");
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Person getPerson() {
        System.out.println("Enter person id:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Person person = null;
        try {
            if (!personDao.checkPersonExist(userId)) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                person = personDao.getPersonById(userId);
            }
            System.out.println("Person:" + "\n" + "- personId: " + person.getId() + "\n" + "- name: "
                    + person.getName() + "\n" + "- personStatus: " + person.getPersonStatus());
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        return person;
    }

    @Override
    public void deletePerson(Person person) {
        try {
            if (!personDao.checkPersonExist(person.getId())) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                personDao.deletePerson(person);
            }
        } catch (DaoException e) {
            System.err.println(e);
        }
    }

    @Transactional
    @Override
    public void updatePersonAndCreateCar(Person person) {
        if (isUpdateEnabled) {
            try {
                person.setPersonStatus(PersonStatus.ACTIVATED);
                System.out.println(person.getPersonStatus());
                Car car = carService.createCar(person);
                personDao.updatePersonAndCar(person, car);
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
