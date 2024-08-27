package com.andersen.tr.service;

import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;

import java.time.LocalDate;

public interface CarServiceInterface {

    public Car createCar(Person person);

    public Car saveCar(Person person);

    public void deleteCar(Person person);

    public void showCar(Person person);

    public void updateCar(Person person);

    public LocalDate detectCarReleaseTime();

    public void extractTicketData();

}
