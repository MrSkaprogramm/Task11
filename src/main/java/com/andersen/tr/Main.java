package com.andersen.tr;

import com.andersen.tr.config.SpringConfig;
import com.andersen.tr.model.Person;
import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {
    private final CarService carService;
    private final PersonService personService;

    @Autowired
    public Main(CarService carService, PersonService personService) {
        this.carService = carService;
        this.personService = personService;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Main main = context.getBean(Main.class);
        main.run();
    }

    private void run(){
        System.out.println("Hello! You in Postgre SQL Demo App");
        savePerson();

        Person person = personService.getPerson();

        updatePersonAndCreateCar(person);

        saveCar(person);
        extractTicketData();
        deletePerson(person);
        deleteCar(person);
        showCar(person);
        updateCar(person);
    }

    private void savePerson(){
            personService.savePerson();
    }

    private void saveCar(Person person){
        for(int i = 0; i < 3; i++) {
            carService.saveCar(person);
        }
    }

    private void deletePerson(Person person){

        personService.deletePerson(person);
    }

    private void updatePersonAndCreateCar(Person person){

        personService.updatePersonAndCreateCar(person);
    }

    private void deleteCar(Person person){

        carService.deleteCar(person);
    }

    private void showCar(Person person){

        carService.showCar(person);
    }

    private void updateCar(Person person){

        carService.updateCar(person);
    }

    private void extractTicketData(){

        carService.extractTicketData();
    }
}
