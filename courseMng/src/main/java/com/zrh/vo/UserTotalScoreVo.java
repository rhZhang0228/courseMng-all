package com.zrh.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTotalScoreVo implements Serializable {
    private Double credits;

    private Double point;

    private Double max;

    private Double min;

    private Double average;
}
