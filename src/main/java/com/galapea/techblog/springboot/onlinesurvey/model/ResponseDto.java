package com.galapea.techblog.springboot.onlinesurvey.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ResponseDto {
  String id;
  String respondentId;
  String surveyId;
  Date startedAt;
  Date completedAt;
  List<AnswerQuestion> answers;
}
