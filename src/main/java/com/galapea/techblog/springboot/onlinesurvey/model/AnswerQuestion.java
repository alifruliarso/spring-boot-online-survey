package com.galapea.techblog.springboot.onlinesurvey.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AnswerQuestion {
  String questionText;
  Integer position;
  String answerText;
  String questionId;
}
