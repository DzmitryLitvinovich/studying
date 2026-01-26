package com.studing.users.service;

import com.studing.users.entity.Role;
import com.studing.users.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}