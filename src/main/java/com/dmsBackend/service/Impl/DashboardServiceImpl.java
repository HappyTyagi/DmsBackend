package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.repository.*;
import com.dmsBackend.response.DashboardResponse;
import com.dmsBackend.service.DashboardService;
import com.dmsBackend.service.DocumentHeaderService;
import com.dmsBackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DocumentHeaderService documentHeaderService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    DocumentDetailsRepository documentDetailRepository;

    @Autowired
    DocumentHeaderRepository documentHeaderRepository;

    @Autowired
    BranchMasterRepository branchMasterRepository;

    @Autowired
    CategoryMasterRepository categoryMasterRepository;

    @Autowired
    DepartmentMasterRepository departmentMasterRepository;

    @Autowired
    RoleMasterRepository roleMasterRepository;

    @Autowired
    TypeMasterRepository typeMasterRepository;

    @Autowired
    YearMasterRepository yearMasterRepository;

    @Override
    public DashboardResponse getAllUsers(String employeeId) {
        DashboardResponse dashboardResponse = new DashboardResponse();

        // Convert employeeId from String to Integer
        Integer empId = Integer.parseInt(employeeId);

        // Find employee and branch ID
        Employee employee = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
        Integer branchId = employee.getBranch().getId();

        // Set general counts
        dashboardResponse.setTotalUser(employeeRepository.count());
        dashboardResponse.setTotalDocument(documentHeaderRepository.count());
        dashboardResponse.setPendingDocument(documentDetailRepository.count());
        dashboardResponse.setStorageUsed(documentDetailRepository.count());
        dashboardResponse.setTotalBranches(branchMasterRepository.count());
        dashboardResponse.setTotalDepartment(departmentMasterRepository.count());
        dashboardResponse.setTotalRoles(roleMasterRepository.count());
        dashboardResponse.setDocumentType(typeMasterRepository.count());
        dashboardResponse.setAnnualYear(yearMasterRepository.count());
        dashboardResponse.setTotalCategories(categoryMasterRepository.count());
        dashboardResponse.setTotalApprovedDocuments(documentHeaderService.countApprovedDocuments());
        dashboardResponse.setTotalRejectedDocuments(documentHeaderService.countRejectedDocuments());
        dashboardResponse.setTotalPendingDocuments(documentHeaderService.countPendingDocuments());
        dashboardResponse.setTotalNullEmployeeType(employeeService.countEmployeesByRoleNull());

        // Set branch-specific counts
        dashboardResponse.setBranchUser(employeeRepository.countByBranchId(branchId));
        dashboardResponse.setDepartmentCountForBranch(departmentMasterRepository.countByBranchId(branchId));
        dashboardResponse.setNullRoleEmployeeCountForBranch(employeeRepository.countByBranchIdAndRoleIsNull(branchId));

        // Set branch-specific document counts
        dashboardResponse.setTotalDocumentsById(documentHeaderService.countDocumentHeadersByBranchId(branchId));
        dashboardResponse.setTotalPendingDocumentsById(documentHeaderService.countPendingDocumentsByBranchId(branchId));
        dashboardResponse.setTotalApprovedStatusDocById(documentHeaderService.countApprovedByBranchId(branchId));
        dashboardResponse.setTotalRejectedStatusDocById(documentHeaderService.countRejectedByBranchId(branchId));

        return dashboardResponse;
    }
}