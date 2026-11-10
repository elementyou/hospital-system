
package com.hospital.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 医院门诊就诊系统 - 启动类
 *
 * @author hospital
 */
@SpringBootApplication
@MapperScan("com.hospital.admin.mapper")
@ComponentScan(basePackages = {"com.hospital.admin", "com.hospital.common"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("========================================");
        System.out.println("医院门诊就诊系统启动成功!");
        System.out.println("接口文档地址: http://localhost:8080/doc.html");
        System.out.println("========================================");
    }
}