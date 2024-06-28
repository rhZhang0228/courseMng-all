package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.dto.SubjectQueryParam;
import com.zrh.entity.Subject;
import com.zrh.utils.PagingResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【subject(问题)】的数据库操作Service
 * @createDate 2024-04-07 19:28:39
 */
public interface SubjectService extends IService<Subject> {
    /**
     * 分页获取list
     *
     * @param subjectQueryParam
     * @return
     */
    PagingResult<Subject> getSubjectList(SubjectQueryParam subjectQueryParam);

    /**
     * 更新
     *
     * @param subject
     */
    void update(Subject subject);

    /**
     * 新增
     *
     * @param subject
     */
    void add(Subject subject);

    /**
     * 删除
     *
     * @param ids
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 所有type
     *
     * @return
     */
    List<String> getAllType();

    /**
     * 生成试卷
     *
     * @param withAnswer
     * @param ids
     * @param response
     */
    void generatePaper(Boolean withAnswer, List<Integer> ids, HttpServletResponse response);
}
