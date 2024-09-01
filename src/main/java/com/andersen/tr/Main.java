package com.andersen.tr;

import com.andersen.tr.service.impl.CarService;
import com.andersen.tr.service.impl.PersonService;
import com.andersen.tr.service.impl.TicketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}
