package com.galapea.techblog.springboot.onlinesurvey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringbootOnlineSurveyApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootOnlineSurveyApplication.class, args);
  }
}
