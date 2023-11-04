package com.galapea.techblog.springboot.onlinesurvey.controller;

import com.galapea.techblog.springboot.onlinesurvey.service.AnswerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/answers")
public class AnswerApiController {
  private final AnswerService answerService;

  public AnswerApiController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @GetMapping()
  ResponseEntity<?> getAnswers() {
    var data = answerService.getAnswers("");
    return ResponseEntity.ok(ApiResponse.builder().data(data).build());
  }
}
