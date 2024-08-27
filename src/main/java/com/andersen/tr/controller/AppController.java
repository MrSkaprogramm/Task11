package com.andersen.tr.controller;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.TicketData;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import com.andersen.tr.service.impl.TicketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class AppController {
    private final PersonService personService;
    private final CarService carService;
    private final TicketDataService ticketDataService;

    @Autowired
    public AppController(PersonService personService, CarService carService, TicketDataService ticketDataService) {
        this.personService = personService;
        this.carService = carService;
        this.ticketDataService = ticketDataService;
    }

    @GetMapping("/savePerson")
    public Person savePerson() {
        return personService.savePerson();
    }

    @GetMapping("/getSingleCar")
    public Car getSingleCar() {
        return personService.getSingleCar();
    }

    @GetMapping("/getPerson")
    public Person getPerson() {
        return personService.getPerson();
    }

    @GetMapping("/deletePerson")
    public String deletePerson() {
        return personService.deletePerson(personService.getPerson());
    }

    @GetMapping("/updatePersonAndCreateCar")
    public String updatePersonAndCreateCar() {
        return personService.updatePersonAndCreateCar(personService.getPerson());
    }

    @GetMapping("/saveCar")
    public Car createCar() {
        return carService.saveCar(personService.getPerson());
    }

    @GetMapping("/showCar")
    public Car showCar() {
        return carService.showCar(personService.getPerson());
    }

    @GetMapping("/deleteCar")
    public String deleteCar() {
        return carService.deleteCar(personService.getPerson());
    }

    @GetMapping("/updateCar")
    public Car updateCar() {
        return carService.showCar(personService.getPerson());
    }

    @GetMapping("/extractTicketData")
    public List<TicketData> extractTicketData() {
        return ticketDataService.extractTicketData();
    }
}
