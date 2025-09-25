package com.galapea.techblog.springboot.onlinesurvey.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Survey {
  String id;
  String title;
  String description;
  boolean isActive;
  Date createdAt;
}
