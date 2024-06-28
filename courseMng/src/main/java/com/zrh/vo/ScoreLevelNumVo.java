package com.zrh.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreLevelNumVo implements Serializable {
    /**
     * 成绩分布位置，如：优秀(85-100)
     */
    private String label;

    /**
     * 个数，如：优秀有1个
     */
    private Integer value;
}
