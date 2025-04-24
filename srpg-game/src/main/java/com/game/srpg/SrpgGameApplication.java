package com.game.srpg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.game.srpg.config.GameDataProperties;

@SpringBootApplication
@EnableConfigurationProperties(GameDataProperties.class)
public class SrpgGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrpgGameApplication.class, args);
    }
}
