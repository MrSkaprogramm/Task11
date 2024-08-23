package com.andersen.tr;

import com.andersen.tr.config.SpringConfig;
import com.andersen.tr.model.User;
import com.andersen.tr.service.impl.TicketService;
import com.andersen.tr.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Component
public class Main {
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public Main(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        Main main = context.getBean(Main.class);
        main.run();
    }

    private void run(){
        System.out.println("Hello! You in Postgre SQL Demo App");
        //saveUser();

        User user = userService.getUser();

        //updateUserAndCreateTicket(user);

        //saveTicket(user);
        extractTicketData();
        //deleteUser(user);
        //deleteTicket(user);
        //showTicket(user);
        //updateTicket(user);
    }

    private void saveUser(){
            userService.saveUser();
    }

    private void saveTicket(User user){
        for(int i = 0; i < 3; i++) {
            ticketService.saveTicket(user);
        }
    }

    private void deleteUser(User user){

        userService.deleteUser(user);
    }

    private void updateUserAndCreateTicket(User user){

        userService.updateUserAndCreateTicket(user);
    }

    private void deleteTicket(User user){

        ticketService.deleteTicket(user);
    }

    private void showTicket(User user){

        ticketService.showTicket(user);
    }

    private void updateTicket(User user){

        ticketService.updateTicket(user);
    }

    private void extractTicketData(){

        ticketService.extractTicketData();
    }


}
