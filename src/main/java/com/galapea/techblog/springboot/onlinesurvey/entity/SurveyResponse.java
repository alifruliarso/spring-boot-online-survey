package com.galapea.techblog.springboot.onlinesurvey.entity;

import com.toshiba.mwcloud.gs.RowKey;
import java.util.Date;
import lombok.Data;

@Data
public class SurveyResponse {
  @RowKey String id;
  String respondentId;
  String surveyId;
  Date startedAt;
  Date completedAt;
}
