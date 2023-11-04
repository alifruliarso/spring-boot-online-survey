package com.galapea.techblog.springboot.onlinesurvey.model;

import lombok.Data;

@Data
public class QuestionCreateRequest {
  String surveyId;
  String questionText;
  Integer position;
}
