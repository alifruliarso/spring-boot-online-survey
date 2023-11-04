package com.galapea.techblog.springboot.onlinesurvey.model;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AnswerDto {
  String id;
  String surveyResponseId;
  String questionId;
  Date createdAt;
  String answer;
  QuestionDto question;
}
