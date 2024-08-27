package com.andersen.tr.service.impl;

import com.andersen.tr.model.TicketData;
import com.andersen.tr.repository.DaoException;
import com.andersen.tr.repository.TicketDataRepository;
import com.andersen.tr.service.TicketDataServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketDataService implements TicketDataServiceInterface {
    private final TicketDataRepository ticketDataRepository;

    @Autowired
    public TicketDataService(TicketDataRepository ticketDataRepository) {
        this.ticketDataRepository = ticketDataRepository;
    }


    @Override
    public List<TicketData> extractTicketData() {
        String jsonFilePath = "classpath:ticketData.txt";
        List<TicketData> ticketDataList = null;
        try {
            ticketDataList = ticketDataRepository.extractTicketData(jsonFilePath);
        } catch (DaoException e) {
            System.err.println(e.getMessage());
        }

        return ticketDataList;
    }
}
