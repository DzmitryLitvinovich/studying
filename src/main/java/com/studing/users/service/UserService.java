package com.studing.users.service;

import com.studing.users.entity.Role;
import com.studing.users.entity.User;
import com.studing.users.entity.UserRole;
import com.studing.users.repository.UserRepository;
import com.studing.users.repository.RoleRepository;
import com.studing.users.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<User> findAllActive() {
        return userRepository.findByActiveTrue();
    }

    @Transactional
    public void addRoleToUser(Long userId, Long roleId) {
        boolean alreadyHasRole = userRoleRepository
                .existsByUserIdAndRoleIdAndActiveTrue(userId, roleId);

        if (alreadyHasRole) {
            throw new RuntimeException("User already has this role");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setActive(true);

        userRoleRepository.save(userRole);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        UserRole userRole = userRoleRepository
                .findActiveByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new RuntimeException("Role not assigned to user"));

        userRole.setActive(false);

        userRoleRepository.save(userRole);
    }
}