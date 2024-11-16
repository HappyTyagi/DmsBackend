package com.dmsBackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class DepartmentMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;


    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    @Column(name="is_Active")
    private int isActive;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "branch_master_id", referencedColumnName = "id")
//    private BranchMaster branch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_master_id", referencedColumnName = "id", nullable = true)
    @JsonBackReference // Prevent recursion
    private BranchMaster branch;

//    @OneToMany(mappedBy = "department")
//    private List<Employee> employees;
}