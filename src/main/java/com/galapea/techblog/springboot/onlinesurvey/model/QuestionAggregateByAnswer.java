package com.galapea.techblog.springboot.onlinesurvey.model;

import lombok.Data;

@Data
public class QuestionAggregateByAnswer {
  private final Long count;
  private final String label;
}
