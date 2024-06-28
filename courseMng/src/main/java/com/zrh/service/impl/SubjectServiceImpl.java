package com.zrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.SubjectQueryParam;
import com.zrh.entity.Subject;
import com.zrh.mapper.SubjectMapper;
import com.zrh.service.SubjectService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangronghao
 * @description 针对表【subject(问题)】的数据库操作Service实现
 * @createDate 2024-04-07 19:28:39
 */
@Service
@Slf4j
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
        implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private RedisTemplate<String, Subject> redisTemplate;
    private static final String TEMPLATE_PATH = "templates/template.docx";

    @Override
    public PagingResult<Subject> getSubjectList(SubjectQueryParam subjectQueryParam) {
        StringBuilder keySb = new StringBuilder();
        keySb.append(RedisConstant.SUBJECT_LIST);
        String type = subjectQueryParam.getType();
        Integer currentPage = subjectQueryParam.getCurrentPage();
        Integer pageSize = subjectQueryParam.getPageSize();
        if (StringUtils.isNotEmpty(type)) {
            keySb.append("::").append(type);
        }
        String key = keySb.toString();
        //从缓存取数据
        if (redisTemplate.hasKey(key)) {
            return RedisUtils.selectPageByCurrentPage(redisTemplate, key, currentPage, pageSize);
        }
        Page<Subject> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(type), Subject::getType, type);
        wrapper.orderByAsc(Subject::getType).orderByAsc(Subject::getId);
        page = subjectMapper.selectPage(page, wrapper);
        RedisUtils.insertList(redisTemplate, subjectMapper.selectList(wrapper), key);
        return new PagingResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void update(Subject subject) {
        if (null == subject.getId()) {
            log.error("错误，id不能为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.SUBJECT_LIST);
        subjectMapper.updateById(subject);
    }

    @Override
    public void add(Subject subject) {
        RedisUtils.deleteAll(redisTemplate, RedisConstant.SUBJECT_LIST);
        subjectMapper.insert(subject);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("ids不能为空");
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.SUBJECT_LIST);
        subjectMapper.deleteBatchIds(ids);
    }

    @Override
    public List<String> getAllType() {
        List<String> typeList = subjectMapper.selectList(null).stream()
                .map(subject -> subject.getType())
                .distinct()
                .collect(Collectors.toList());
        return typeList;
    }

    @Override
    public void generatePaper(Boolean withAnswer, List<Integer> ids, HttpServletResponse response) {
        if (null == withAnswer) {
            log.error("withAnswer不能为空");
            return;
        }
        if (CollectionUtils.isEmpty(ids)) {
            log.error("ids不能为空");
            return;
        }
        LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Subject::getId, ids);
        List<Subject> subjectList = subjectMapper.selectList(wrapper);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        try {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode("试卷.docx", StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        List<String> typeList = subjectList.stream().map(Subject::getType).distinct().sorted((q1, q2) -> {
            // 定义题目类型的优先级顺序
            String[] priority = {"选择题", "填空题", "简答题"};
            int index1 = -1;
            int index2 = -1;
            // 获取题目类型的优先级顺序索引
            for (int i = 0; i < priority.length; i++) {
                if (q1.equals(priority[i])) {
                    index1 = i;
                }
                if (q2.equals(priority[i])) {
                    index2 = i;
                }
            }
            // 如果两个题目都是优先级题目，则按照题目类型的优先级顺序排列
            if (index1 != -1 && index2 != -1) {
                return Integer.compare(index1, index2);
            }
            // 如果其中一个题目是优先级题目，将优先级题目排在前面
            else if (index1 != -1) {
                return -1;
            } else if (index2 != -1) {
                return 1;
            }
            // 如果都不是优先级题目，则按照字母顺序排列
            else {
                return q1.compareTo(q2);
            }
        }).collect(Collectors.toList());
        // 创建一个新的 Word 文档对象
        XWPFDocument document = new XWPFDocument();
        // 创建大标题段落对象
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        // 创建大标题文本运行对象
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("重庆邮电大学试卷");
        titleRun.setFontSize(36);
        titleRun.setBold(true);
        for (String type : typeList) {
            int cnt = 1;
            // 创建小标题段落对象
            XWPFParagraph subTitleParagraph = document.createParagraph();
            subTitleParagraph.setAlignment(ParagraphAlignment.LEFT);
            // 创建小标题文本运行对象
            XWPFRun subTitleRun = subTitleParagraph.createRun();
            subTitleRun.setText(type);
            subTitleRun.setFontSize(18);
            subTitleRun.setBold(true);
            List<Subject> correctSubjectList = subjectList.stream().filter(subject -> type.equals(subject.getType())).collect(Collectors.toList());
            for (Subject subject : correctSubjectList) {
                StringBuilder sb = new StringBuilder();
                // 添加正文文字
                XWPFParagraph contentParagraph = document.createParagraph();
                contentParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun contentRun = contentParagraph.createRun();
                sb.append(cnt).append("、").append(subject.getQuestion());
                contentRun.setText(sb.toString());
                contentRun.addBreak();
                if (withAnswer) {
                    sb = new StringBuilder();
                    sb.append("答案：").append(subject.getAnswer()).append("\n\n");
                    contentRun.setText(sb.toString());
                }
                cnt++;
            }
        }
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        try {
            document.write(os);
        } catch (IOException e) {
            log.error("写入word失败");
            log.error(e.getMessage(), e);
        }
        try {
            os.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        try {
            document.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}




