package com.carsales.backend.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.carsales.backend.mapper")
public class MybatisConfig {
}
