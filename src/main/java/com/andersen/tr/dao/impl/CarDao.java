package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Car;
import com.andersen.tr.dao.CarDaoInterface;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.TicketData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CarDao implements CarDaoInterface {
    private static final String SELECT_CAR_BY_ID_QUERY = "SELECT FROM Car WHERE id = :id AND person.id = :personId";
    private static final String CHECK_CAR_EXIST_QUERY = "SELECT COUNT(*) FROM Car WHERE id = :ticketNum AND person.id = :userId";

    private final SessionFactory sessionFactory;

    public CarDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveCar(Car car) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.save(car);
        } catch (Exception e) {
            throw new DaoException("Error saving car: " + e.getMessage());
        }
    }

    @Override
    public void updateCar(Car car) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.update(car);
        } catch (Exception e) {
            throw new DaoException("Error updating car: " + e.getMessage());
        }
    }

    @Override
    public void deleteCar(Car car) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            session.delete(car);
        } catch (Exception e) {
            throw new DaoException("Error deleting car: " + e.getMessage());
        }
    }

    @Override
    public Car fetchCarById(int id, Person person) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            Query<Car> query = session.createQuery(SELECT_CAR_BY_ID_QUERY, Car.class);
            query.setParameter("id", id);
            query.setParameter("personId", person.getId());
            List<Car> cars = query.getResultList();
            if (!cars.isEmpty()) {
                return cars.get(0);
            } else {
                throw new DaoException("Car not found: " + id);
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching car: " + e.getMessage());
        }
    }

    @Override
    public boolean checkCarExist(int ticketNum, int userId) throws DaoException {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(CHECK_CAR_EXIST_QUERY, Long.class);
            query.setParameter("ticketNum", ticketNum);
            query.setParameter("userId", userId);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Error checking if car exists", e);
        }
    }

    public List<TicketData> extractTicketData(String jsonFilePath) throws DaoException {
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
