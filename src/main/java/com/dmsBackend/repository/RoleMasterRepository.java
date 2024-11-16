package com.dmsBackend.repository;

import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer> {

    Optional<RoleMaster> findByRole(String role);

    List<RoleMaster> findByIsActive(boolean isActive);
}
