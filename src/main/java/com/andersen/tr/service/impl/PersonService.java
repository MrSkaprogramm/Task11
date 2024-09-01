package com.andersen.tr.service.impl;

import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.repository.PersonRepository;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.PersonStatus;
import com.andersen.tr.service.PersonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class PersonService implements PersonServiceInterface {
    static Scanner scanner = new Scanner(System.in);
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
    public Person savePerson() {
        System.out.println("Enter person name:");
        String name = scanner.nextLine();
        PersonStatus personStatus = PersonStatus.DEACTIVATED;
        String password = "1234";
        Person person = new Person(name, personStatus, password);

        person = personRepository.save(person);

        System.out.println("Person created");
        return person;
    }

    @Override
    public Person getPerson() {
        System.out.println("Enter person id:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Person person = null;
        Optional<Person> optionalPerson = personRepository.findById(userId);
        if (optionalPerson.isPresent()) {
            person = optionalPerson.get();
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        System.out.println("Person:" + "\n" + "- personId: " + person.getId() + "\n" + "- name: "
                + person.getName() + "\n" + "- personStatus: " + person.getStatus());
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
    public String updatePersonAndCreateCar(Person person) {
        person.setStatus(PersonStatus.ACTIVATED);
        person = personRepository.save(person);
        Car car = carService.createCar(person);
        car = carRepository.save(car);
        return "Person: " + person.toString() + "car: " + car.toString();
    }

    @Override
    public Car getSingleCar() {
        System.out.println("Enter person id:");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Optional<Person> optionalPerson = personRepository.findById(userId);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            List<Car> cars = person.getCars();
            if (!cars.isEmpty()) {
                Car car = cars.get(0);
                System.out.println("Car:" + "\n" + "- carId: " + car.getId() + "\n" + "- make: " + car.getBrand() + "\n" + "- model: " + car.getType());
                return car;
            } else {
                throw new IllegalArgumentException("The user does not have a car");
            }
        } else {
            throw new IllegalArgumentException("Person not found");
        }
    }
}
