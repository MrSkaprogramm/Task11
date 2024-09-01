package com.andersen.tr.service;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;

import java.time.LocalDate;

public interface CarServiceInterface {

    public Car createCar(Person person, CarType carType, String carBrandString);

    public Car saveCar(Person person, CarType carType, String carBrandString);

    public String deleteCar(int carNum);

    public Car showCar(int carNum);

    public Car updateCar(int carNum, String carBrand, CarType carType);

    public LocalDate detectCarReleaseTime();

    public Car getPersonCar(Person person);

}
