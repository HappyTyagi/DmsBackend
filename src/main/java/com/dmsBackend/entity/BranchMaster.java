package com.dmsBackend.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "branch_master")
public class BranchMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name="is_Active")
    private int isActive;

    @Column(name = "address")
    private String address;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

//    @OneToMany(mappedBy = "branch")
//    @JsonManagedReference
//    private List<Employee> employees;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents serialization of employees to avoid recursion
    private List<Employee> employees;

    @OneToMany(mappedBy = "branch")
    @JsonManagedReference // Break the recursive serialization
    private List<DepartmentMaster> departments;

}