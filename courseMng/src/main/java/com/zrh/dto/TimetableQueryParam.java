package com.zrh.dto;

import lombok.Data;

@Data
public class TimetableQueryParam {
    private String studentName;

    private Integer year;

    private Integer term;

    private Integer week;

    private String teacherId;

    private String profession;

    private String grade;
}
