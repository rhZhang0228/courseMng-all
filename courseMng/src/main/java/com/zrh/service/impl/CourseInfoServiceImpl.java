package com.zrh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.entity.CourseInfo;
import com.zrh.mapper.CourseInfoMapper;
import com.zrh.service.CourseInfoService;
import org.springframework.stereotype.Service;

/**
 * @author zhangronghao
 * @description 针对表【CourseInfoService】的数据库操作Service实现
 * @createDate 2024-03-25 13:58:04
 */
@Service
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo>
        implements CourseInfoService {

}
