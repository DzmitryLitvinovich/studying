package com.studing.users.repository;

import com.studing.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByName(String name);
    List<User> findByActiveTrue();

    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.roles r WHERE u.id = :userId AND r.id = :roleId")
    boolean hasRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM User u JOIN u.roles r WHERE u.username = :username AND r.name = :roleName")
    boolean hasRoleByName(@Param("username") String username, @Param("roleName") String roleName);
}