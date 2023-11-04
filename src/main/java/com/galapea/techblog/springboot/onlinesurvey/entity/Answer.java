package com.galapea.techblog.springboot.onlinesurvey.entity;

import com.toshiba.mwcloud.gs.RowKey;
import java.util.Date;
import lombok.Data;

@Data
public class Answer {
  @RowKey String id;
  String surveyResponseId;
  String questionId;
  Date createdAt;
  String answer;
}
