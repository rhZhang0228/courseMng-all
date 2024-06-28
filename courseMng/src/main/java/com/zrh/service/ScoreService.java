package com.zrh.service;

import com.zrh.dto.CourseListQueryParam;
import com.zrh.dto.Score;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.dto.UserTotalQueryParam;
import com.zrh.entity.Course;
import com.zrh.utils.PagingResult;
import com.zrh.vo.ScoreLevelNumVo;
import com.zrh.vo.UserTotalScoreVo;

import java.util.List;

public interface ScoreService {
    /**
     * 根据成绩拿到等级（不及格、及格、优秀）的个数
     *
     * @param scoreLevelNumDto
     * @return
     */
    List<ScoreLevelNumVo> getScoreLevelNum(ScoreLevelNumDto scoreLevelNumDto);

    /**
     * 导出成绩到Excel
     *
     * @param scoreLevelNumDto
     * @return
     */
    List<Course> getExportList(ScoreLevelNumDto scoreLevelNumDto);

    /**
     * 根据班级等信息获取课程信息
     *
     * @param courseListQueryParam
     * @param offset
     * @param limit
     * @return
     */
    PagingResult<Course> getCourseList(CourseListQueryParam courseListQueryParam, Integer offset, Integer limit);

    /**
     * 学生用户根据id获取总学分和总绩点
     *
     * @param userTotalQueryParam
     * @return
     */
    UserTotalScoreVo getUserTotal(UserTotalQueryParam userTotalQueryParam);

    /**
     * 成绩录入
     *
     * @param scoreList
     */
    void addEntry(List<Score> scoreList);
}
