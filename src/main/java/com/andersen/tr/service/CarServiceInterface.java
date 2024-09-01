package com.andersen.tr.service;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;

import java.time.LocalDate;

public interface CarServiceInterface {

    public Car createCar(Person person);

    public Car saveCar(Person person);

    public String deleteCar(Person person);

    public Car showCar(Person person);

    public Car updateCar(Person person);

    public LocalDate detectCarReleaseTime();

}
