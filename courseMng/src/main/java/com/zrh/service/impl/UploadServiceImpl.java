package com.zrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.entity.Upload;
import com.zrh.mapper.UploadMapper;
import com.zrh.service.UploadService;
import com.zrh.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangronghao
 * @description 针对表【upload】的数据库操作Service实现
 * @createDate 2024-03-25 14:25:26
 */
@Service
@Slf4j
public class UploadServiceImpl extends ServiceImpl<UploadMapper, Upload>
        implements UploadService {
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public String getHeader(String userId, String level) {
        LambdaQueryWrapper<Upload> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Upload::getUserId, userId).eq(Upload::getLevel, level);
        Upload upload = uploadMapper.selectOne(queryWrapper);
        if (null == upload) {
            return "";
        }
        try {
            return minioUtils.getPresignedObjectUrl(upload.getObjectName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    @Override
    @Transactional
    public String upload(MultipartFile file, Integer level, String userId) {
        if (file.isEmpty()) {
            return "";
        }
        String filename = file.getOriginalFilename();
        String objectName = level + "/" + userId + "/" + filename;
        LambdaQueryWrapper<Upload> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Upload::getUserId, userId).eq(Upload::getLevel, level);
        Upload upload = uploadMapper.selectOne(queryWrapper);
        try {
            if (null == upload) {
                minioUtils.uploadFile(file.getInputStream(), objectName);
                String url = minioUtils.getPresignedObjectUrl(objectName);
                upload = new Upload();
                upload.setUserId(userId);
                upload.setLevel(level);
                upload.setUrl(url);
                log.debug("url--->{}", url);
                upload.setObjectName(objectName);
                uploadMapper.insert(upload);
            } else {
                String oldObjectName = upload.getObjectName();
                minioUtils.deleteObject(oldObjectName);
                minioUtils.uploadFile(file.getInputStream(), objectName);
                String url = minioUtils.getPresignedObjectUrl(objectName);
                upload.setUrl(url);
                upload.setObjectName(objectName);
                uploadMapper.updateById(upload);
            }
            return minioUtils.getPresignedObjectUrl(objectName);
        } catch (Exception e) {
            log.error("上传失败");
            throw new RuntimeException(e);
        }
    }
}