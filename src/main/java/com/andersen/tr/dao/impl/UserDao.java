package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.model.User;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.dao.UserDaoInterface;
import com.andersen.tr.model.UserStatus;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class UserDao implements UserDaoInterface {
    private static final String SAVE_USER_SQL = "INSERT INTO \"User\" (name, user_status) VALUES (?, ?)";
    private static final String GET_USER_BY_ID_SQL = "SELECT id, name, user_status FROM \"User\" WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM \"User\" WHERE id = ?";
    private static final String CHECK_USER_EXIST_SQL = "SELECT COUNT(*) FROM \"User\" WHERE id = ?";
    private static final String UPDATE_USER_SQL = "UPDATE \"User\" SET name = ?, user_status = ? WHERE id = ?";
    private static final String INSERT_TICKET_QUERY = "INSERT INTO Ticket (ticket_class, ticket_type, start_date, user_id) VALUES (?, ?, ?, ?)";

    private final DataSource dataSource;
    private final TicketDao ticketDao;

    public UserDao(DataSource dataSource, TicketDao ticketDao) {
        this.dataSource = dataSource;
        this.ticketDao = ticketDao;
    }


    @Override
    public void saveUser(User user) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_USER_SQL)) {
            statement.setString(1, user.getName());
            statement.setObject(2, user.getUserStatus(), Types.OTHER);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error saving user", e);
        }
    }

    @Override
    public User getUserById(int id) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    UserStatus userStatus = UserStatus.valueOf(resultSet.getString("user_status"));
                    return new User(userId, name, userStatus);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching user by ID", e);
        }
    }

    @Override
    public void deleteUser(User user) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USER_SQL)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting user", e);
        }
    }

    @Override
    public void updateUserAndTickets(User user, Ticket ticket) throws DaoException {
        try {
            updateUser(user);
            ticketDao.saveTicket(ticket);
        } catch (Exception e) {
            throw new DaoException("Error updating user and tickets", e);
        }
    }

    @Override
    public boolean checkUserExist(int userId) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_USER_EXIST_SQL)) {
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
    public void updateUser(User user) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL)) {
            statement.setString(1, user.getName());
            statement.setObject(2, user.getUserStatus(), Types.OTHER);
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating user", e);
        }
    }
}
