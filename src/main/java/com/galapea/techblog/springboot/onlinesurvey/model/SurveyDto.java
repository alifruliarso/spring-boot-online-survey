package com.galapea.techblog.springboot.onlinesurvey.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SurveyDto {
  String id;
  String title;
  String description;
  boolean isActive;
  Date createdAt;
  List<QuestionDto> questions;
}
