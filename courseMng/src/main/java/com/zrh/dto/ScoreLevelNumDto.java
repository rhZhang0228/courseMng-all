package com.zrh.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("查询成绩分布时传入的参数")
@NoArgsConstructor
@AllArgsConstructor
public class ScoreLevelNumDto implements Serializable {
    private String level;

    private String studentName;

    private String year;

    private String term;

    private String profession;

    private String grade;

    private String courseName;

    private String username;

}
