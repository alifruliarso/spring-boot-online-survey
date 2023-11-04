package com.galapea.techblog.springboot.onlinesurvey.controller;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ApiResponse<T> {
  T data;
  String error;
}
