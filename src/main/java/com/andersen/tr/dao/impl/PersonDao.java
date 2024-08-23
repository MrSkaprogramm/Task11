package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Car;
import com.andersen.tr.model.Person;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.PersonDaoInterface;
import com.andersen.tr.model.PersonStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class PersonDao implements PersonDaoInterface {
    private static final String SAVE_PERSON_SQL = "INSERT INTO \"Person\" (name, person_status) VALUES (?, ?)";
    private static final String GET_PERSON_BY_ID_SQL = "SELECT id, name, person_status FROM \"Person\" WHERE id = ?";
    private static final String DELETE_PERSON_SQL = "DELETE FROM \"Person\" WHERE id = ?";
    private static final String CHECK_PERSON_EXIST_SQL = "SELECT COUNT(*) FROM \"Person\" WHERE id = ?";
    private static final String UPDATE_PERSON_SQL = "UPDATE \"Person\" SET name = ?, person_status = ? WHERE id = ?";

    private final DataSource dataSource;
    private final CarDao carDao;

    public PersonDao(DataSource dataSource, CarDao carDao) {
        this.dataSource = dataSource;
        this.carDao = carDao;
    }


    @Override
    public void savePerson(Person person) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_PERSON_SQL)) {
            statement.setString(1, person.getName());
            statement.setObject(2, person.getPersonStatus(), Types.OTHER);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error saving person", e);
        }
    }

    @Override
    public Person getPersonById(int id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PERSON_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    PersonStatus personStatus = PersonStatus.valueOf(resultSet.getString("person_status"));
                    return new Person(userId, name, personStatus);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching user by ID", e);
        }
    }

    @Override
    public void deletePerson(Person person) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PERSON_SQL)) {
            statement.setInt(1, person.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting person", e);
        }
    }

    @Override
    public void updatePersonAndCar(Person person, Car car) throws DaoException {
        try {
            updatePerson(person);
            carDao.saveCar(car);
        } catch (Exception e) {
            throw new DaoException("Error updating person and tickets", e);
        }
    }

    @Override
    public boolean checkPersonExist(int userId) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_PERSON_EXIST_SQL)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error checking if user exists", e);
        }
    }

    @Override
    public void updatePerson(Person person) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON_SQL)) {
            statement.setString(1, person.getName());
            statement.setObject(2, person.getPersonStatus(), Types.OTHER);
            statement.setInt(3, person.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating person", e);
        }
    }
}
