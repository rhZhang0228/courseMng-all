package com.zrh.controller;

import com.zrh.dto.CourseListQueryParam;
import com.zrh.dto.Score;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.dto.UserTotalQueryParam;
import com.zrh.entity.Course;
import com.zrh.service.ScoreService;
import com.zrh.utils.PagingResult;
import com.zrh.vo.ScoreLevelNumVo;
import com.zrh.vo.UserTotalScoreVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms/score")
@Api(tags = "成绩相关")
@Slf4j
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @ApiOperation("根据成绩拿到等级（不及格、及格、优秀）的个数")
    @GetMapping("getUserNum")
    public List<ScoreLevelNumVo> getScoreLevelNum(ScoreLevelNumDto scoreLevelNumDto) {
        return scoreService.getScoreLevelNum(scoreLevelNumDto);
    }

    @ApiOperation("淡出个人的成绩")
    @GetMapping("/export")
    public List<Course> getExportList(ScoreLevelNumDto scoreLevelNumDto) {
        return scoreService.getExportList(scoreLevelNumDto);
    }

    @GetMapping("/getCourseList")
    @ApiOperation("成绩信息")
    public PagingResult<Course> getCourseList(CourseListQueryParam courseListQueryParam,
                                              @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
                                              @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
        return scoreService.getCourseList(courseListQueryParam, offset, limit);
    }

    @GetMapping("/getUserTotal")
    @ApiOperation("成绩总分")
    public UserTotalScoreVo getUserTotal(UserTotalQueryParam userTotalQueryParam) {
        return scoreService.getUserTotal(userTotalQueryParam);
    }

    @ApiOperation("成绩录入")
    @PostMapping
    private void addEntry(@RequestBody List<Score> scoreList) {
        scoreService.addEntry(scoreList);
    }
}
