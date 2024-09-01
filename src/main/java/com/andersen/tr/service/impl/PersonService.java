package com.andersen.tr.service.impl;

import com.andersen.tr.model.CarType;
import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.repository.PersonRepository;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.PersonStatus;
import com.andersen.tr.service.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService implements PersonServiceInterface {
    private final boolean isUpdateEnabled;
    private final CarService carService;
    private final PersonRepository personRepository;
    private final CarRepository carRepository;

    @Autowired
    public PersonService(boolean isUpdateEnabled, CarService carService, PersonRepository personRepository, CarRepository carRepository) {
        this.isUpdateEnabled = isUpdateEnabled;
        this.carService = carService;
        this.personRepository = personRepository;
        this.carRepository = carRepository;
    }

    @Transactional
    @Override
    public Person savePerson(String name, PersonStatus personStatus, String password) {
        Person person = new Person(name, personStatus, password);

        person = personRepository.save(person);

        System.out.println("Person created");
        return person;
    }

    @Override
    public Person getPerson(int personId) {
        Person person = null;
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            person = optionalPerson.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
        return person;
    }

    @Transactional
    @Override
    public String deletePerson(Person person) {
        personRepository.deleteById(person.getId());
        return "Person deleted";
    }

    @Transactional
    @Override
    public String updatePersonAndCreateCar(Person person, CarType carType, String carBrandString) {
        if (isUpdateEnabled) {
            person.setStatus(PersonStatus.ACTIVATED);
            person = personRepository.save(person);
            Car car = carService.createCar(person, carType, carBrandString);
            car = carRepository.save(car);
            return "Person: " + person.toString() + "car: " + car.toString();
        } else {
            throw new IllegalArgumentException("You are not be able to do it!");
        }
    }
}
