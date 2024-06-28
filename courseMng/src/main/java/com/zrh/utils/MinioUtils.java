package com.zrh.utils;


import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 创建一个桶
     */
    public void createBucket(String bucket) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 上传一个文件
     */
    public void uploadFile(InputStream stream, String bucket, String objectName) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
                .stream(stream, -1, 10485760).build());
    }

    /**
     * 上传一个文件
     */
    public void uploadFile(InputStream stream, String objectName) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
                .stream(stream, -1, 10485760).build());
    }

    /**
     * 列出所有的桶
     */
    public List<String> listBuckets() throws Exception {
        List<Bucket> list = minioClient.listBuckets();
        List<String> names = new ArrayList<>();
        list.forEach(b -> {
            names.add(b.name());
        });
        return names;
    }


    /**
     * 下载一个文件
     */
    public InputStream download(String bucket, String objectName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        return stream;
    }


    /**
     * 下载一个文件
     */
    public InputStream download(String objectName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        return stream;
    }

    /**
     * 删除一个桶
     */
    public void deleteBucket(String bucket) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }

    /**
     * 删除一个对象
     */
    public void deleteObject(String bucket, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
    }

    /**
     * 删除一个对象
     */
    public void deleteObject(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
    }


    /**
     * 复制文件
     *
     * @Param: [sourceBucket, sourceObject, targetBucket, targetObject]
     * @return: void
     * @Author: MrFugui
     * @Date: 2021/11/15
     */
    public void copyObject(String sourceBucket, String sourceObject, String targetBucket, String targetObject) throws Exception {
        this.createBucket(targetBucket);
        minioClient.copyObject(CopyObjectArgs.builder().bucket(targetBucket).object(targetObject)
                .source(CopySource.builder().bucket(sourceBucket).object(sourceObject).build()).build());
    }

    /**
     * 获取文件信息
     *
     * @Param: [bucket, objectName]
     * @return: java.lang.String
     * @Author: MrFugui
     * @Date: 2021/11/15
     */
    public String getObjectInfo(String bucket, String objectName) throws Exception {

        return minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build()).toString();

    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。
     *
     * @Param: [bucketName, objectName, expires]
     * @return: java.lang.String
     * @Author: MrFugui
     * @Date: 2021/11/15
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires) throws Exception {
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder().bucket(bucketName).object(objectName).expiry(expires).method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(build);
    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。
     *
     * @Param: [bucketName, objectName, expires]
     * @return: java.lang.String
     * @Author: MrFugui
     * @Date: 2021/11/15
     */
    public String getPresignedObjectUrl(String objectName) throws Exception {
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder().bucket(bucket).object(objectName).method(Method.GET).build();
        String url = minioClient.getPresignedObjectUrl(build);
        log.debug("url为------>{}", url);
        return url;
    }

    public static void main(String[] args) {
        String url = "http://minio:9000/course-mng/0/1/4f60c9ba-d99c-4dac-9767-4d833a430d90.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20240417%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240417T071349Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=3f8e26c0ca25af146054f3cdc4ac8fd79da0b5e90bc35f4adcaf4f5f8934b896";
        if (url.contains("minio:9000")) {
            url = url.replace("minio:9000", "localhost:9000");
        }
        System.out.println(url);
    }
}
