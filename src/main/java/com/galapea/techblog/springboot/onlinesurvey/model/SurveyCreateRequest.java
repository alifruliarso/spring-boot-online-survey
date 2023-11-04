package com.galapea.techblog.springboot.onlinesurvey.model;

import lombok.Data;

@Data
public class SurveyCreateRequest {
  String title;
  String description;
  boolean isActive = true;
}
