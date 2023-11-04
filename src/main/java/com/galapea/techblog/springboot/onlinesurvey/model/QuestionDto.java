package com.galapea.techblog.springboot.onlinesurvey.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class QuestionDto {
  String id;
  String surveyId;
  String questionText;
  Integer position;
}
