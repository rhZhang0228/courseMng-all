package com.zrh;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.zrh.mapper")
@EnableCaching
@EnableSwagger2Doc
public class CourseMngApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseMngApplication.class, args);
    }

}
