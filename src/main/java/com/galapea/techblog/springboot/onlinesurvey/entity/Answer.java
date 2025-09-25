package com.galapea.techblog.springboot.onlinesurvey.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Answer {
  String id;
  String surveyResponseId;
  String questionId;
  Date createdAt;
  String answer;
}
