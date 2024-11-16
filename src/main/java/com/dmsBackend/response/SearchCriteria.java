package com.dmsBackend.response;

import lombok.Data;

@Data
public class SearchCriteria {
    private String fileNo;
    private String title;
    private String subject;
    private String version;
    private String category;
}
