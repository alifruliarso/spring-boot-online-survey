package com.galapea.techblog.springboot.onlinesurvey.entity;

import com.toshiba.mwcloud.gs.RowKey;
import lombok.Data;

@Data
public class Question {
  @RowKey String id;
  String surveyId;
  String questionText;
  Integer position;
}
