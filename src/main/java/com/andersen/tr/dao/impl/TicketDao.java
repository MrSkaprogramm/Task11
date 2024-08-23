package com.andersen.tr.dao.impl;
import com.andersen.tr.model.Ticket;
import com.andersen.tr.dao.TicketDaoInterface;
import com.andersen.tr.dao.DaoException;
import com.andersen.tr.model.TicketData;
import com.andersen.tr.model.TicketType;
import com.andersen.tr.model.User;
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
public class TicketDao implements TicketDaoInterface {
    private static final String INSERT_TICKET_QUERY = "INSERT INTO \"Ticket\" (ticket_class, ticket_type, start_date, user_id) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TICKET_QUERY = "UPDATE Ticket SET ticket_class = ?, ticket_type = ?, start_date = ?, user_id = ? WHERE id = ?";
    private static final String SELECT_TICKET_BY_ID_QUERY = "SELECT * FROM Ticket WHERE id = ?";
    private static final String CHECK_TICKET_EXIST_QUERY = "SELECT COUNT(*) FROM Ticket WHERE id = ? AND user_id = ?";
    private static final String DELETE_TICKET_QUERY = "DELETE FROM Ticket WHERE id = ?";
    private final DataSource dataSource;

    public TicketDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveTicket(Ticket ticket) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TICKET_QUERY)) {
            statement.setString(1, ticket.getTicketClass());
            statement.setObject(2, ticket.getTicketType(), Types.OTHER);
            statement.setDate(3, java.sql.Date.valueOf(ticket.getStartDate()));
            statement.setInt(4, ticket.getUser().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error saving ticket: " + e.getMessage());
        }
    }

    @Override
    public void updateTicket(Ticket ticket) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TICKET_QUERY)) {
            statement.setString(1, ticket.getTicketClass());
            statement.setObject(2, ticket.getTicketType(), Types.OTHER);
            statement.setDate(3, java.sql.Date.valueOf(ticket.getStartDate()));
            statement.setInt(4, ticket.getUser().getId());
            statement.setInt(5, ticket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error updating ticket: " + e.getMessage());
        }
    }

    @Override
    public void deleteTicket(Ticket ticket) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TICKET_QUERY)) {
            statement.setInt(1, ticket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Error deleting ticket: " + e.getMessage());
        }
    }

    @Override
    public Ticket fetchTicketById(int id, User user) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TICKET_BY_ID_QUERY)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String ticketClass = resultSet.getString("ticket_class");
                TicketType ticketType = TicketType.valueOf(resultSet.getString("ticket_type"));
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                int userId = resultSet.getInt("user_id");

                return new Ticket(id, ticketClass, ticketType, startDate, user);
            } else {
                throw new DaoException("Ticket not found: " + id);
            }
        } catch (SQLException e) {
            throw new DaoException("Error fetching ticket: " + e.getMessage());
        }
    }

    @Override
    public boolean checkTicketExist(int ticketNum, int userId) throws DaoException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_TICKET_EXIST_QUERY)) {
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
