package com.dmsBackend.controller;


import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.response.DepartmentResponse;
import com.dmsBackend.service.DepartmentMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/DepartmentMaster")
//@CrossOrigin("https://happytyagi.github.io/DmsFrontend/")
public class DepartmentMasterController {
    @Autowired
    DepartmentMasterService departmentMasterService;

    @PostMapping("/save")
    public ResponseEntity<DepartmentMaster> createDepartmentMaster(@RequestBody DepartmentMaster departmentMaster) {
        DepartmentMaster saveDepartmentMaster = this.departmentMasterService.saveDepartmentMaster(departmentMaster);
        return new ResponseEntity<DepartmentMaster>(saveDepartmentMaster, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DepartmentMaster> updateDepartmentMaster(@PathVariable Integer id, @RequestBody DepartmentMaster departmentMaster) {
        try {
            DepartmentMaster updateddepartmentMaster = departmentMasterService.updateDepartmentMaster(departmentMaster, id);
            return new ResponseEntity<>(updateddepartmentMaster, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DepartmentMaster> deletebyIdDepartmentMaster(@PathVariable Integer id) {
        this.departmentMasterService.deleteByIdDepartmentMaster(id);
        return new ResponseEntity(new ApiResponse("DepartmentMaster deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DepartmentResponse>> findAllBranchMaster() {
        List<DepartmentResponse> allDepartmentMasterMaster = this.departmentMasterService.findAllDepartmentMaster();
        return new ResponseEntity(allDepartmentMasterMaster, HttpStatus.OK);
    }

    @GetMapping("/findActiveRole")
    public ResponseEntity<List<DepartmentMaster>>findAllActiveRole(){
        List<DepartmentMaster>allActiveDeptMaster = this.departmentMasterService.findAllActiveDepartmentMaster(1);
        return new ResponseEntity<>(allActiveDeptMaster, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<DepartmentMaster> findByIdDepartmentMaster(@PathVariable Integer id) {
        Optional<DepartmentMaster> departmentMasterMaster = departmentMasterService.findDepartmentMasterById(id);
        return departmentMasterMaster.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updateDeptStatus/{id}")
    public ResponseEntity<DepartmentMaster> updateStatusDepartment(
            @PathVariable Integer id,
            @RequestBody Integer isActive) {
        try {
            DepartmentMaster departmentMaster = this.departmentMasterService.updateStatusDepartment(id, isActive);
            return new ResponseEntity<>(departmentMaster, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/findByBranch/{branchId}")
    public List<DepartmentMaster> findByBranch(@PathVariable Integer branchId) {
        return departmentMasterService.findDepartmentMasterByBranch(branchId);
    }
}