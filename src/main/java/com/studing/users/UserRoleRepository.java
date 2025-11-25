package com.studing.users;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRoleRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String GET_USER_ROLES = """
        SELECT r.*
        FROM roles r
            JOIN user_roles ur ON r.id = ur.role_id
        WHERE ur.user_id = ? AND ur.active = true
    """;
    private static final String ADD_ROLE_TO_USER = """
        INSERT INTO user_roles (
            user_id,
            role_id,
            active,
            created
            )
        VALUES (?, ?, true, ?)
    """;
    private static final String REMOVE_ROLE_FROM_USER = """
        UPDATE user_roles
        SET
            active = false,
            updated = ?
        WHERE
            user_id = ? AND
            role_id = ? AND
            active = true
    """;
    private static final String CHECK_USER_ROLE = """
        SELECT 1
        FROM user_roles
        WHERE
            user_id = ? AND
            role_id = ? AND
            active = true
    """;

    private Role fillRole(ResultSet resultSet) throws SQLException {
        var role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        return role;
    }

    public List<Role> getUserRoles(Long userId) throws SQLException {
        var roles = new ArrayList<Role>();
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(GET_USER_ROLES)
        ) {
            preparedStatement.setLong(1, userId);
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var role = fillRole(resultSet);
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public void addRoleToUser(Long userId, Long roleId) throws SQLException {
        if (hasUserRole(userId, roleId)) {
            System.out.println("User already has this role");
            return;
        }

        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(ADD_ROLE_TO_USER)
        ) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
        }
    }

    public void removeRoleFromUser(Long userId, Long roleId) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(REMOVE_ROLE_FROM_USER)
        ) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, roleId);
            preparedStatement.executeUpdate();
        }
    }

    public boolean hasUserRole(Long userId, Long roleId) throws SQLException {
        try (
                var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                var preparedStatement = connection.prepareStatement(CHECK_USER_ROLE)
        ) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);
            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}