package com.studing.users.repository;

import com.studing.users.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId AND ur.active = true")
    Optional<UserRole> findActiveByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.active = true")
    List<UserRole> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.role.name = :roleName AND ur.active = true")
    List<UserRole> findActiveByRoleName(@Param("roleName") String roleName);

    boolean existsByUserIdAndRoleIdAndActiveTrue(Long userId, Long roleId);
}