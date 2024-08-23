package com.andersen.tr.service.impl;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.TicketData;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.impl.CarDao;
import com.andersen.tr.service.CarServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Service
public class CarService implements CarServiceInterface {
    static Scanner scanner = new Scanner(System.in);

    private final CarDao carDao;

    @Autowired
    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    @Override
    public Car createCar(Person person) {
        System.out.println("Enter car type:");
        String typeString = scanner.nextLine();
        CarType carType = CarType.valueOf(typeString.toUpperCase());
        System.out.println("Enter car class:");
        String ticketClassString = scanner.nextLine();
        LocalDate creationDate = detectCarReleaseTime();

        Car car = new Car();
        car.setCarBrand(ticketClassString);
        car.setCarType(carType);
        car.setReleaseDate(creationDate);
        car.setPerson(person);

        return car;
    }

    @Override
    public Car saveCar(Person person) {
        Car car = createCar(person);

        try {
            carDao.saveCar(car);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Car created");

        return car;
    }

    @Transactional
    @Override
    public void deleteCar(Person person) {
        System.out.println("Which car you want to delete?");
        int carNum = scanner.nextInt();
        scanner.nextLine();
        System.out.println(person.getCars().size());
        try {
            carDao.deleteCar(person.getCars().get(carNum));
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void showCar(Person person) {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        Car car = null;
        try {
            if (!carDao.checkCarExist(carNum, person.getId())) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                car = carDao.fetchCarById(person.getCars().get(carNum).getId(), person);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Car with " + "car id " + car.getId() + "\n" + "- userId: "
                + car.getPerson().getId() + "\n" + "- carType: " + car.getCarType() + "\n" + "- releaseDate: "
                + car.getReleaseDate() + "\n" + "- carBrand: "
                + car.getCarBrand());
    }

    @Transactional
    @Override
    public void updateCar(Person person) {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter car class:");
        String ticketClassString = scanner.nextLine().toUpperCase();

        System.out.println("Enter type of car:");
        String type = scanner.nextLine();
        CarType carType = CarType.valueOf(type.toUpperCase());

        try {
            if (!(carDao.checkCarExist(carNum, person.getId()))) {
                throw new IllegalArgumentException("Wrong id!");
            } else {
                Car car = person.getCars().get(carNum);
                car.setCarType(carType);
                car.setCarBrand(ticketClassString);
                carDao.updateCar(car);
            }
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public LocalDate detectCarReleaseTime(){
        LocalDate releaseDate = LocalDate.now();
        return releaseDate;
    }

    @Override
    public void extractTicketData() {
        String jsonFilePath = "classpath:ticketData.txt";
        List<TicketData> ticketDataList = null;
        try {
            ticketDataList = carDao.extractTicketData(jsonFilePath);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }

        for (TicketData ticketData : ticketDataList) {
            System.out.println("Ticket Class: " + ticketData.getTicketClass());
            System.out.println("Ticket Type: " + ticketData.getTicketType());
            System.out.println("Start Date: " + ticketData.getStartDate());
            System.out.println("Price: " + ticketData.getPrice());
            System.out.println();
        }
    }
}