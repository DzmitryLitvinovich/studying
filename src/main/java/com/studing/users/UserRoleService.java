package com.studing.users;

import java.sql.SQLException;
import java.util.List;

public class UserRoleService {
    private final UserRepository userRepository = new UserRepository();
    private final RoleRepository roleRepository = new RoleRepository();
    private final UserRoleRepository userRoleRepository = new UserRoleRepository();

    public void assignRoleToUser(String username, String roleName) throws SQLException {
        var user = userRepository.findByUsername(username);
        var role = roleRepository.findByName(roleName);

        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        if (role == null) {
            throw new SQLException("Role not found: " + roleName);
        }

        userRoleRepository.addRoleToUser(user.getId(), role.getId());
    }

    public void removeRoleFromUser(String username, String roleName) throws SQLException {
        var user = userRepository.findByUsername(username);
        var role = roleRepository.findByName(roleName);

        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        if (role == null) {
            throw new SQLException("Role not found: " + roleName);
        }

        userRoleRepository.removeRoleFromUser(user.getId(), role.getId());
    }

    public List<Role> getUserRoles(String username) throws SQLException {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        return userRoleRepository.getUserRoles(user.getId());
    }

    public boolean userHasRole(String username, String roleName) throws SQLException {
        var user = userRepository.findByUsername(username);
        var role = roleRepository.findByName(roleName);

        if (user == null || role == null) {
            return false;
        }

        return userRoleRepository.hasUserRole(user.getId(), role.getId());
    }
}