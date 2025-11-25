package com.studing.users;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String FIND_ALL = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SAVE = """
        INSERT INTO users (
            name,
            surname,
            age,
            username,
            password,
            inserted_date_at_utc)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
    private static final String UPDATE = """
        UPDATE users
        SET
            name = ?,
            surname = ?,
            age = ?,
            username = ?,
            password = ?,
            updated_date_at_utc = ?
        WHERE id = ?
    """;
    private static final String DELETE = "DELETE FROM users WHERE id = ?";

    private User fillUser(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setAge(resultSet.getInt("age"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setInsertedDateAtUtc(resultSet.getTimestamp("inserted_date_at_utc").toLocalDateTime());

        var updatedTimestamp = resultSet.getTimestamp("updated_date_at_utc");
        if (updatedTimestamp != null) {
            user.setUpdatedDateAtUtc(updatedTimestamp.toLocalDateTime());
        }

        return user;
    }

    private void buildSaveQuery(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setInt(3, user.getAge());
        preparedStatement.setString(4, user.getUsername());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
    }

    public List<User> findAll() throws SQLException {
        var users = new ArrayList<User>();
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(FIND_ALL)
        ) {
            while (resultSet.next()) {
                var user = fillUser(resultSet);
                users.add(user);
            }
        }
        return users;
    }

    public User findById(Long id) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(FIND_BY_ID)
        ) {
            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillUser(resultSet);
                }
                return null;
            }
        }
    }

    public User findByUsername(String username) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(FIND_BY_USERNAME)
        ) {
            preparedStatement.setString(1, username);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillUser(resultSet);
                }
                return null;
            }
        }
    }

    public void save(User user) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)
        ) {
            buildSaveQuery(preparedStatement, user);  // ← ТЕПЕРЬ ИСПОЛЬЗУЕТСЯ!
            preparedStatement.executeUpdate();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    var id = generatedKeys.getLong(1);
                    user.setId(id);
                }
            }
        }
    }

    public void update(User user) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(UPDATE)
        ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setLong(7, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(DELETE)
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }
}