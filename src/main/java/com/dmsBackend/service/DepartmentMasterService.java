package com.dmsBackend.service;

import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.response.DepartmentResponse;

import java.util.List;
import java.util.Optional;

public interface DepartmentMasterService {
    DepartmentMaster saveDepartmentMaster(DepartmentMaster departmentMaster);
    DepartmentMaster updateDepartmentMaster(DepartmentMaster departmentMaster,Integer id);
    void deleteByIdDepartmentMaster(Integer id);
    List<DepartmentResponse> findAllDepartmentMaster();
    List<DepartmentMaster> findDepartmentMasterByBranch(Integer branchId);

    List<DepartmentMaster> findAllActiveDepartmentMaster(Integer isActive);

    Optional<DepartmentMaster> findDepartmentMasterById(Integer id);

    DepartmentMaster findByIdDep(Integer id);
    DepartmentMaster updateStatusDepartment(Integer id, Integer isApproved);
}