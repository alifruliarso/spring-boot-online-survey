package com.galapea.techblog.springboot.onlinesurvey.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.galapea.techblog.springboot.onlinesurvey.entity.Survey;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class SurveyService {
  private Map<String, Survey> surveyCollection;
  private QuestionService questionService;

  public SurveyService(Map<String, Survey> surveyCollection, QuestionService questionService) {
    this.surveyCollection = surveyCollection;
    this.questionService = questionService;
  }

  public String create(SurveyCreateRequest request) {
    Survey survey = new Survey();
    survey.setId(KeyGenerator.next("survey"));
    survey.setActive(request.isActive());
    survey.setTitle(request.getTitle());
    survey.setDescription(request.getDescription());
    survey.setCreatedAt(new Date());
    log.info("put: {}", survey);
    surveyCollection.put(survey.getId(), survey);
    return survey.getId();
  }

  public boolean deleteSurvey(String surveyId) {
    return surveyCollection.remove(surveyId) != null;
  }

  public SurveyDto getSurvey(String surveyId) {
    Survey survey = surveyCollection.get(surveyId);
    if (survey == null) {
      throw new ResponseStatusException(NOT_FOUND, "Not found");
    }
    List<QuestionDto> questions = questionService.getQuestions(survey.getId());
    return SurveyDto.builder()
        .id(survey.getId())
        .createdAt(survey.getCreatedAt())
        .description(survey.getDescription())
        .title(survey.getTitle())
        .isActive(survey.isActive())
        .questions(questions)
        .build();
  }

  public List<SurveyDto> getSurveys() {
    List<SurveyDto> result = new ArrayList<>();
    for (Survey survey : surveyCollection.values()) {
      result.add(
          SurveyDto.builder()
              .id(survey.getId())
              .createdAt(survey.getCreatedAt())
              .description(survey.getDescription())
              .title(survey.getTitle())
              .isActive(survey.isActive())
              .questions(new ArrayList<>())
              .build());
    }
    return result;
  }
}
