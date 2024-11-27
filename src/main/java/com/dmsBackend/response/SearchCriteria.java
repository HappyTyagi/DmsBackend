package com.dmsBackend.response;

import com.dmsBackend.entity.CategoryMaster;
import lombok.Data;

@Data
public class SearchCriteria {
    private String fileNo;
    private String title;
    private String subject;
    private String version;
    private Integer categoryId;
    private Integer branchId;
    private Integer departmentId;

    // Getters and setters
}
