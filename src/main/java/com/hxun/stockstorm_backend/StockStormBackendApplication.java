package com.hxun.stockstorm_backend;

import cn.dev33.satoken.SaManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockStormBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockStormBackendApplication.class, args);
        System.out.println(SaManager.getConfig());
    }

}
