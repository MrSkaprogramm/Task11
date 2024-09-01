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
import java.util.List;
import java.util.Optional;

@Service
public class CarService implements CarServiceInterface {
    private final CarRepository carRepository;
    private final String conditionalBean;

    @Autowired
    public CarService(CarRepository carRepository, @Autowired(required = false) String conditionalBean) {
        this.carRepository = carRepository;
        this.conditionalBean = conditionalBean;
    }

    @Override
    public Car createCar(Person person, CarType carType, String carBrandString) {
        if (person == null || carType == null || carBrandString == null || carBrandString.isEmpty()) {
            throw new IllegalArgumentException();
        }

        LocalDate creationDate = detectCarReleaseTime();

        Car car = new Car();
        car.setBrand(carBrandString.trim());
        car.setType(carType);
        car.setReleaseDate(creationDate);
        car.setPerson(person);

        return car;
    }

    @Override
    public Car saveCar(Person person, CarType carType, String carBrandString) {
        if (person == null || carType == null || carBrandString == null || carBrandString.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Car car = createCar(person, carType, carBrandString);
        car = carRepository.save(car);
        return car;
    }

    @Transactional
    @Override
    public String deleteCar(int carId) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isPresent()) {
            carRepository.deleteById(carId);
            return "Car deleted";
        }
        return "Car not found";
    }

    @Override
    public Car showCar(int carNum) {
        Car car = null;
        Optional<Car> optionalCar = carRepository.findById(carNum);
        if (optionalCar.isPresent()) {
            car = optionalCar.get();
        } else {
            throw new IllegalArgumentException("Car not found");
        }
        return car;
    }

    @Transactional
    @Override
    public Car updateCar(int carNum, String carBrand, CarType carType) {
        Optional<Car> optionalCar = carRepository.findById(carNum);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setId(carNum);
            if (carBrand != null) {
                car.setBrand(carBrand.trim());
            }
            car.setType(carType);
            return carRepository.save(car);
        } else {
            throw new IllegalArgumentException("Car not found");
        }
    }

    @Override
    public Car getPersonCar(Person person) {
            List<Car> cars = person.getCars();
            if (!cars.isEmpty()) {
                Car car = cars.get(0);
                return car;
            } else {
                throw new IllegalArgumentException("The user does not have a car");
            }
    }

    @Override
    public LocalDate detectCarReleaseTime(){
        LocalDate releaseDate = LocalDate.now();
        return releaseDate;
    }
}