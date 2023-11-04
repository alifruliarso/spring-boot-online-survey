package com.galapea.techblog.springboot.onlinesurvey.controller;

import com.galapea.techblog.springboot.onlinesurvey.model.SurveyCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyResponseService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/surveys")
public class SurveyApiController {
  private final SurveyService surveyService;
  private final SurveyResponseService surveyResponseService;

  public SurveyApiController(
      SurveyService surveyService, SurveyResponseService surveyResponseService) {
    this.surveyService = surveyService;
    this.surveyResponseService = surveyResponseService;
  }

  @GetMapping()
  ResponseEntity<?> getSurveys() {
    var data = surveyService.getSurveys();
    return ResponseEntity.ok(ApiResponse.builder().data(data).build());
  }

  @GetMapping("/{surveyId}")
  ResponseEntity<?> getSurvey(@PathVariable("surveyId") String surveyId) {
    var data = surveyService.getSurvey(surveyId);
    return ResponseEntity.ok(ApiResponse.builder().data(data).build());
  }

  @PostMapping()
  ResponseEntity<?> createSurvey(@RequestBody SurveyCreateRequest req) {
    var data = surveyService.create(req);
    return ResponseEntity.ok(ApiResponse.builder().data(data).build());
  }

  @GetMapping("/{surveyId}/response")
  ResponseEntity<?> getSurveyResponse(@PathVariable("surveyId") String surveyId) {
    var data = surveyResponseService.getResponses(surveyId);
    return ResponseEntity.ok(ApiResponse.builder().data(data).build());
  }
}
