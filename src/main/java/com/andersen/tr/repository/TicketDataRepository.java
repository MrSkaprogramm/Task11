package com.andersen.tr.repository;

import com.andersen.tr.model.TicketData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.repository.CrudRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public interface TicketDataRepository extends CrudRepository<TicketData, Integer> {
    default List<TicketData> extractTicketData(String jsonFilePath) throws DaoException {
        List<TicketData> ticketDataList = new ArrayList<>();
        ResourceLoader resourceLoader = new DefaultResourceLoader();

        try {
            Resource resource = resourceLoader.getResource(jsonFilePath);
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                ObjectMapper objectMapper = new ObjectMapper();

                String line;
                while ((line = reader.readLine()) != null) {
                    JsonNode node = objectMapper.readTree(line);

                    String ticketClass = node.get("ticketClass") != null ? node.get("ticketClass").asText() : "";
                    String ticketType = node.get("ticketType") != null ? node.get("ticketType").asText() : "";
                    String startDate = node.get("startDate") != null ? node.get("startDate").asText() : "";
                    double price = node.get("price") != null ? node.get("price").asDouble() : 0.0;

                    TicketData ticketData = new TicketData(ticketClass, ticketType, startDate, price);
                    ticketDataList.add(ticketData);
                }
            } catch (IOException e) {
                throw new DaoException(e);
            }
            return ticketDataList;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
