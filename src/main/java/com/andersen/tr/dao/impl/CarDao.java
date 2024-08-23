package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Car;
import com.andersen.tr.dao.CarDaoInterface;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.model.CarType;
import com.andersen.tr.model.Person;
import com.andersen.tr.model.TicketData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String INSERT_CAR_QUERY = "INSERT INTO \"Car\" (car_brand, car_type, release_date, person_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CAR_QUERY = "UPDATE Car SET car_brand = ?, car_type = ?, release_date = ?, person_id = ? WHERE id = ?";
    private static final String SELECT_CAR_BY_ID_QUERY = "SELECT * FROM Car WHERE id = ?";
    private static final String CHECK_CAR_EXIST_QUERY = "SELECT COUNT(*) FROM Car WHERE id = ? AND person_id = ?";
    private static final String DELETE_CAR_QUERY = "DELETE FROM Car WHERE id = ?";
    private final DataSource dataSource;

    public CarDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveCar(Car car) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CAR_QUERY)) {
            statement.setString(1, car.getCarBrand());
            statement.setObject(2, car.getCarType(), Types.OTHER);
            statement.setDate(3, java.sql.Date.valueOf(car.getReleaseDate()));
            statement.setInt(4, car.getPerson().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error saving car: " + e.getMessage());
        }
    }

    @Override
    public void updateCar(Car car) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CAR_QUERY)) {
            statement.setString(1, car.getCarBrand());
            statement.setObject(2, car.getCarType(), Types.OTHER);
            statement.setDate(3, java.sql.Date.valueOf(car.getReleaseDate()));
            statement.setInt(4, car.getPerson().getId());
            statement.setInt(5, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating car: " + e.getMessage());
        }
    }

    @Override
    public void deleteCar(Car car) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CAR_QUERY)) {
            statement.setInt(1, car.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting car: " + e.getMessage());
        }
    }

    @Override
    public Car fetchCarById(int id, Person person) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_CAR_BY_ID_QUERY)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String ticketClass = resultSet.getString("car_brand");
                CarType carType = CarType.valueOf(resultSet.getString("car_type"));
                LocalDate startDate = resultSet.getDate("release_date").toLocalDate();
                int userId = resultSet.getInt("person_id");

                return new Car(id, ticketClass, carType, startDate, person);
            } else {
                throw new DaoException("Car not found: " + id);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching car: " + e.getMessage());
        }
    }

    @Override
    public boolean checkCarExist(int ticketNum, int userId) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_CAR_EXIST_QUERY)) {
            statement.setInt(1, ticketNum);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking if ticket exists", e);
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
