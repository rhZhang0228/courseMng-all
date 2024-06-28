package com.zrh.controller;

import com.zrh.dto.SubjectQueryParam;
import com.zrh.entity.Subject;
import com.zrh.service.SubjectService;
import com.zrh.utils.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/sms/subject")
@Api(tags = "题库")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("getSubjectList")
    @ApiOperation("分页查询")
    public PagingResult<Subject> getSubjectList(SubjectQueryParam subjectQueryParam) {
        return subjectService.getSubjectList(subjectQueryParam);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    public void update(@RequestBody Subject subject) {
        subjectService.update(subject);
    }

    @PostMapping("add")
    @ApiOperation("新增")
    public void add(@RequestBody Subject subject) {
        subjectService.add(subject);
    }

    @DeleteMapping
    @ApiOperation("删除")
    public void deleteByIds(@RequestParam("ids") List<Integer> ids) {
        subjectService.deleteByIds(ids);
    }

    @GetMapping("getAllType")
    public List<String> getAllType() {
        return subjectService.getAllType();
    }

    @GetMapping("generatePaper")
    @ApiOperation("生成试卷")
    public void generatePaper(@ApiParam("是否生成含答案的试卷") @RequestParam Boolean withAnswer,
                              @RequestParam("ids") List<Integer> ids,
                              HttpServletResponse response) {
        subjectService.generatePaper(withAnswer, ids, response);
    }
}
