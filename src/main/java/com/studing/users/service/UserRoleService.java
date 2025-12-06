package com.studing.users.service;

import com.studing.users.entity.Role;
import com.studing.users.entity.User;
import com.studing.users.entity.UserRole;
import com.studing.users.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final RoleService roleService;

    public UserRoleService(UserRoleRepository userRoleRepository,
                           UserService userService,
                           RoleService roleService) {
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public List<Role> getUserRoles(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findActiveByUserId(userId);
        List<Role> roles = new ArrayList<>();

        for (UserRole userRole : userRoles) {
            roles.add(userRole.getRole());
        }

        return roles;
    }

    public List<Role> getUserRoles(String username) {
        User user = findUserByUsername(username);
        return getUserRoles(user.getId());
    }

    public void addRoleToUser(Long userId, Long roleId) {
        boolean alreadyHasRole = userRoleRepository
                .existsByUserIdAndRoleIdAndActiveTrue(userId, roleId);

        if (alreadyHasRole) {
            throw new RuntimeException("User already has this role");
        }

        User user = findUserById(userId);
        Role role = findRoleById(roleId);

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setActive(true);
        userRole.setCreated(LocalDateTime.now());

        userRoleRepository.save(userRole);
    }

    public void addRoleToUser(String username, String roleName) {
        User user = findUserByUsername(username);
        Role role = findRoleByName(roleName);
        addRoleToUser(user.getId(), role.getId());
    }

    public void removeRoleFromUser(Long userId, Long roleId) {
        UserRole userRole = userRoleRepository
                .findActiveByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new RuntimeException("Role not assigned to user"));

        userRole.setActive(false);
        userRole.setUpdated(LocalDateTime.now());
        userRoleRepository.save(userRole);
    }

    public void removeRoleFromUser(String username, String roleName) {
        User user = findUserByUsername(username);
        Role role = findRoleByName(roleName);
        removeRoleFromUser(user.getId(), role.getId());
    }

    public boolean hasUserRole(Long userId, Long roleId) {
        return userRoleRepository.existsByUserIdAndRoleIdAndActiveTrue(userId, roleId);
    }

    public boolean hasUserRole(String username, String roleName) {
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        Role role = roleService.findByName(roleName).orElse(null);
        if (role == null) {
            return false;
        }

        return hasUserRole(user.getId(), role.getId());
    }

    public List<User> getUsersWithRole(String roleName) {
        List<UserRole> userRoles = userRoleRepository.findActiveByRoleName(roleName);

        List<User> users = new ArrayList<>();

        for (UserRole userRole : userRoles) {
            users.add(userRole.getUser());
        }

        return users;
    }

    private User findUserById(Long userId) {
        return userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private User findUserByUsername(String username) {
        return userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private Role findRoleById(Long roleId) {
        return roleService.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
    }

    private Role findRoleByName(String roleName) {
        return roleService.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }
}