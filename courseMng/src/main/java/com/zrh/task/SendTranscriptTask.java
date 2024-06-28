package com.zrh.task;


import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.zrh.entity.Course;
import com.zrh.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SendTranscriptTask implements Runnable {
    private Student student;
    private List<Course> courseList;

    @Override
    public void run() {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("TranscriptTmpl.ftl");
        String result = template.render(Dict.create().set("courseList", courseList));
        log.debug("=================准备发送短信==============");
        log.debug("邮箱--->{}", student.getEmail());
        log.debug("成绩单-->{}", result);
        if (CollectionUtils.isEmpty(courseList)) {
            MailUtil.send(student.getEmail(), student.getRealName() + "的本学期成绩单", "对不起，你本学期的成绩还未录入，请联系任课老师", false);
        } else {
            MailUtil.send(student.getEmail(), student.getRealName() + "的本学期成绩单", result, true);
        }
        log.debug("=================发送完毕==============");
    }

    public static void main(String[] args) {
        List<Course> list = new ArrayList<>();
        Course course = new Course();
        course.setName("计网");
        course.setScoreByUser("78");
        course.setPointByUser("2.00");
        course.setCreditsByUser("4.00");
        course.setType(1);
        course.setProfession("计算机");
        course.setGrade("2016");
        list.add(course);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("TranscriptTmpl.ftl");
        String result = template.render(Dict.create().set("courseList", list));
        MailUtil.send("1809674320@qq.com", "成绩单", result, true);
        System.out.println(result);
    }
}


