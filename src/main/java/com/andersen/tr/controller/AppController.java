package com.andersen.tr.controller;

import com.andersen.tr.model.*;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import com.andersen.tr.service.impl.TicketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/")
public class AppController {
    static Scanner scanner = new Scanner(System.in);

    private final PersonService personService;
    private final CarService carService;
    private final TicketDataService ticketDataService;
    private final String conditionalBean;

    @Autowired
    public AppController(PersonService personService, CarService carService, TicketDataService ticketDataService, String conditionalBean) {
        this.personService = personService;
        this.carService = carService;
        this.ticketDataService = ticketDataService;
        this.conditionalBean = conditionalBean;
    }

    @GetMapping("/savePerson")
    public Person savePerson() {
        System.out.println("Enter person name:");
        String name = scanner.nextLine();
        PersonStatus personStatus = PersonStatus.DEACTIVATED;
        String password = "1234";
        return personService.savePerson(name, personStatus, password);
    }

    @GetMapping("/getPersonCar")
    public Car getPersonCar() {
        return carService.getPersonCar(getPerson());
    }

    @GetMapping("/getPerson")
    public Person getPerson() {
        if (conditionalBean != null) {
            System.out.println(conditionalBean);
        }

        System.out.println("Enter person id:");
        int personId = scanner.nextInt();
        scanner.nextLine();
        return personService.getPerson(personId);
    }

    @GetMapping("/deletePerson")
    public String deletePerson() {
        return personService.deletePerson(getPerson());
    }

    @GetMapping("/updatePersonAndCreateCar")
    public String updatePersonAndCreateCar() {
        System.out.println("Enter car type:");
        String typeString = scanner.nextLine();
        CarType carType = CarType.valueOf(typeString.toUpperCase());
        System.out.println("Enter car brand:");
        String carBrandString = scanner.nextLine();

        return personService.updatePersonAndCreateCar(getPerson(), carType, carBrandString);
    }

    @GetMapping("/saveCar")
    public Car saveCar() {
        System.out.println("Enter car type:");
        String typeString = scanner.nextLine();
        CarType carType = CarType.valueOf(typeString.toUpperCase());
        System.out.println("Enter car brand:");
        String carBrandString = scanner.nextLine();
        return carService.saveCar(getPerson(), carType, carBrandString);
    }

    @GetMapping("/showCar")
    public Car showCar() {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        return carService.showCar(carNum);
    }

    @GetMapping("/deleteCar")
    public String deleteCar() {
        System.out.println("Which car you want to delete?");
        int carNum = scanner.nextInt();
        scanner.nextLine();
        return carService.deleteCar(carNum);
    }

    @GetMapping("/updateCar")
    public Car updateCar() {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter car class:");
        String carBrand = scanner.nextLine().toUpperCase();

        System.out.println("Enter type of car:");
        String type = scanner.nextLine();
        CarType carType = CarType.valueOf(type.toUpperCase());

        return carService.updateCar(carNum, carBrand, carType);
    }

    @GetMapping("/extractTicketData")
    public List<TicketData> extractTicketData() {
        return ticketDataService.extractTicketData();
    }
}
