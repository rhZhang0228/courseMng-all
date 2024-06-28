package com.zrh.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminProfessionInfoVo implements Serializable {
    private String profession;

    private List<Integer> grade;

    private List<String> course;
}
