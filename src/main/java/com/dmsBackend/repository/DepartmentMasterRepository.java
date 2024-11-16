package com.dmsBackend.repository;

import com.dmsBackend.entity.DepartmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentMasterRepository extends JpaRepository<DepartmentMaster,Integer> {

    List<DepartmentMaster> findByIsActive(Integer isActive);

    List<DepartmentMaster> findByBranchId(Integer branchId);

    long countByBranchId(Integer branchId); // Counts departments per branch
}
