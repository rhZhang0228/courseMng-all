package com.zrh.dto;

import lombok.Data;

@Data
public class CourseListQueryParam {
    private String profession;

    private String grade;

    private String username;

    private String courseName;

    private String studentName;

    private String level;

    private Integer year;

    private Integer term;
}
