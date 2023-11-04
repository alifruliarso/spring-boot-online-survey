package com.galapea.techblog.springboot.onlinesurvey.entity;

import com.toshiba.mwcloud.gs.RowKey;
import java.util.Date;
import lombok.Data;

@Data
public class Survey {
  @RowKey String id;
  String title;
  String description;
  boolean isActive;
  Date createdAt;
}
