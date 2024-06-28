package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.entity.Upload;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhangronghao
 * @description 针对表【upload】的数据库操作Service
 * @createDate 2024-03-25 14:25:26
 */
public interface UploadService extends IService<Upload> {
    /**
     * 获取头像url
     *
     * @param userId
     * @param level
     * @return
     */
    String getHeader(String userId, String level);

    /**
     * 上传头像
     *
     * @param file
     * @param level
     * @param userId
     * @return
     */
    String upload(MultipartFile file, Integer level, String userId);
}
