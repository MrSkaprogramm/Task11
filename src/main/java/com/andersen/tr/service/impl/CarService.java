package com.andersen.tr.service.impl;

import com.andersen.tr.repository.CarRepository;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.service.CarServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CarService implements CarServiceInterface {
    static Scanner scanner = new Scanner(System.in);

    private final CarRepository carRepository;
    private final String conditionalBean;

    @Autowired
    public CarService(CarRepository carRepository, @Autowired(required = false) String conditionalBean) {
        this.carRepository = carRepository;
        this.conditionalBean = conditionalBean;
    }

    @Override
    public Car createCar(Person person) {
        if (conditionalBean != null) {
            System.out.println(conditionalBean);
        }

        System.out.println("Enter car type:");
        String typeString = scanner.nextLine();
        CarType carType = CarType.valueOf(typeString.toUpperCase());
        System.out.println("Enter car class:");
        String carBrandString = scanner.nextLine();
        LocalDate creationDate = detectCarReleaseTime();

        Car car = new Car();
        car.setBrand(carBrandString);
        car.setType(carType);
        car.setReleaseDate(creationDate);
        car.setPerson(person);

        return car;
    }

    @Override
    public Car saveCar(Person person) {
        Car car = createCar(person);
        car = carRepository.save(car);
        return car;
    }

    @Transactional
    @Override
    public String deleteCar(Person person) {
        System.out.println("Which car you want to delete?");
        int carNum = scanner.nextInt();
        scanner.nextLine();
        System.out.println(carNum);
        carRepository.deleteById(carNum);
        return "Car deleted";
    }

    @Override
    public Car showCar(Person person) {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        Car car = null;
        Optional<Car> optionalCar = carRepository.findById(carNum);
        if (optionalCar.isPresent()) {
            car = optionalCar.get();
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        return car;
    }

    @Transactional
    @Override
    public Car updateCar(Person person) {
        System.out.println("Enter number of car:");
        int carNum = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter car class:");
        String carBrand = scanner.nextLine().toUpperCase();

        System.out.println("Enter type of car:");
        String type = scanner.nextLine();
        CarType carType = CarType.valueOf(type.toUpperCase());

        Car car;
        Optional<Car> optionalCar = carRepository.findById(carNum);
        if (optionalCar.isPresent()) {
            car = optionalCar.get();
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        car.setBrand(carBrand);
        car.setType(carType);

        System.out.println(car.getId());

        car = carRepository.save(car);

        return car;
    }

    @Override
    public LocalDate detectCarReleaseTime(){
        LocalDate releaseDate = LocalDate.now();
        return releaseDate;
    }
}