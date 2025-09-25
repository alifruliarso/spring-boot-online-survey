package com.galapea.techblog.springboot.onlinesurvey.entity;

import lombok.Data;

@Data
public class Question {
  String id;
  String surveyId;
  String questionText;
  Integer position;
}
