package com.studing.users;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserRoleService {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    private static final String FIND_ALL_USERS = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String SAVE = """
        INSERT INTO users (name, surname, age, username, password, inserted_date_at_utc)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
    private static final String UPDATE = """
        UPDATE users SET name = ?, surname = ?, age = ?, username = ?, password = ?, updated_date_at_utc = ? WHERE id = ?
    """;

    private static final String DELETE = """
        DELETE FROM users WHERE id = ?
    """;

    private static final String FIND_ALL_ROLES = "SELECT * FROM roles";
    private static final String FIND_ROLE_BY_NAME = "SELECT * FROM roles WHERE name = ?";

    private static final String GET_USER_ROLES = """
        SELECT r.* FROM roles r 
        JOIN user_roles ur ON r.id = ur.role_id 
        WHERE ur.user_id = ?
        """;

    private static final String ADD_ROLE_TO_USER = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
    private static final String REMOVE_ROLE_FROM_USER = "DELETE FROM user_roles WHERE user_id = ? AND role_id = ?";
    private static final String CHECK_USER_ROLE = "SELECT 1 FROM user_roles WHERE user_id = ? AND role_id = ?";

    private User fillUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setAge(resultSet.getInt("age"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setInsertedDateAtUtc(resultSet.getTimestamp("inserted_date_at_utc").toLocalDateTime());

        Timestamp updatedTimestamp = resultSet.getTimestamp("updated_date_at_utc");
        if (updatedTimestamp != null) {
            user.setUpdatedDateAtUtc(updatedTimestamp.toLocalDateTime());
        }

        return user;
    }

    private Role fillRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setDescription(resultSet.getString("description"));
        return role;
    }

    private void buildQuery(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setInt(3, user.getAge());
        preparedStatement.setString(4, user.getUsername());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
    }

    public List<User> findAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement(); // ← Исправлено на Statement
             ResultSet resultSet = statement.executeQuery(FIND_ALL_USERS)) {

            while (resultSet.next()) {
                User user = fillUser(resultSet);
                users.add(user);
            }
        }
        return users;
    }

    public User findUserById(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillUser(resultSet);
                }
                return null;
            }
        }
    }

    public User findUserByUsername(String username) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillUser(resultSet);
                }
                return null;
            }
        }
    }

    public void save(User user) throws SQLException {
        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)
        ) {
            buildQuery(preparedStatement, user);

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    user.setId(id);
                }
            }
        }
    }

    public void update(User user) throws SQLException {
        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)
        ) {
            // Устанавливаем значения для UPDATE
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // updated_date_at_utc
            preparedStatement.setLong(7, user.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE)
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public List<Role> findAllRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement(); // ← Исправлено на Statement
             ResultSet resultSet = statement.executeQuery(FIND_ALL_ROLES)) {

            while (resultSet.next()) {
                Role role = fillRole(resultSet);
                roles.add(role);
            }
        }
        return roles;
    }

    public Role findRoleByName(String roleName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ROLE_BY_NAME)) {

            preparedStatement.setString(1, roleName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return fillRole(resultSet);
                }
                return null;
            }
        }
    }

    public List<Role> getUserRoles(Long userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLES)) {

            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Role role = fillRole(resultSet);
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public void addRoleToUser(Long userId, Long roleId) throws SQLException {
        // Сначала проверяем, есть ли уже такая роль
        if (hasUserRole(userId, roleId)) {
            System.out.println("User already has this role");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_ROLE_TO_USER)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);
            preparedStatement.executeUpdate();
        }
    }

    public void removeRoleFromUser(Long userId, Long roleId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_ROLE_FROM_USER)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);
            preparedStatement.executeUpdate();
        }
    }

    private boolean hasUserRole(Long userId, Long roleId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_ROLE)) {

            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, roleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}