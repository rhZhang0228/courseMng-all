package com.zrh.dto;

import lombok.Data;

@Data
public class SubjectQueryParam {
    private Integer currentPage = 1;
    private Integer pageSize = 10;
    private String type;
}
