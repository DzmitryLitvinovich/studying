package com.studing.users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String FIND_ALL = "SELECT * FROM roles";
    private static final String FIND_BY_ID = "SELECT * FROM roles WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM roles WHERE name = ?";

    private Role fillRole(ResultSet resultSet) throws SQLException {
        var role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        return role;
    }

    public List<Role> findAll() throws SQLException {
        var roles = new ArrayList<Role>();
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var statement = connection.createStatement();
                var resultSet = statement.executeQuery(FIND_ALL)
        ) {
            while (resultSet.next()) {
                var role = fillRole(resultSet);
                roles.add(role);
            }
        }
        return roles;
    }

    public Role findById(Long id) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(FIND_BY_ID)
        ) {
            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillRole(resultSet);
                }
                return null;
            }
        }
    }

    public Role findByName(String name) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(FIND_BY_NAME)
        ) {
            preparedStatement.setString(1, name);
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillRole(resultSet);
                }
                return null;
            }
        }
    }
}