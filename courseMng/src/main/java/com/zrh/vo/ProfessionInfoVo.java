package com.zrh.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProfessionInfoVo implements Serializable {
    private String profession;

    private String teacherId;

    private List<String> grade;

    private List<String> course;
}
