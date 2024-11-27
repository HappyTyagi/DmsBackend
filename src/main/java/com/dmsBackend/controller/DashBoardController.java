package com.dmsBackend.controller;

import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.repository.EmployeeRepository;
import com.dmsBackend.response.DashboardResponse;
import com.dmsBackend.service.BranchMasterService;
import com.dmsBackend.service.DashboardService;
import com.dmsBackend.service.DepartmentMasterService;
import com.dmsBackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Dashboard")
@CrossOrigin("https://happytyagi.github.io/DmsFrontend/")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;

    @Autowired
    EmployeeService employeeService;

    @GetMapping("GetAllCountsForDashBoard")
    public ResponseEntity<DashboardResponse> getAllDashBoardCounts(@RequestParam String employeeId) {
        DashboardResponse dashboardResponse = this.dashboardService.getAllUsers(employeeId);
        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }

    // New endpoint for counting users by branch




}
