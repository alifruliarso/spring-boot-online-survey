package com.galapea.techblog.springboot.onlinesurvey.entity;

import java.util.Date;
import lombok.Data;

@Data
public class SurveyResponse {
  String id;
  String respondentId;
  String surveyId;
  Date startedAt;
  Date completedAt;
}
