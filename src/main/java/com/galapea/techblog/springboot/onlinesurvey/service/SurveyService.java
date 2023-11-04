package com.galapea.techblog.springboot.onlinesurvey.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.galapea.techblog.springboot.onlinesurvey.entity.Survey;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyDto;
import com.toshiba.mwcloud.gs.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class SurveyService {
  private Collection<String, Survey> surveyCollection;
  private QuestionService questionService;
  private GridStore gridStore;

  public SurveyService(
      Collection<String, Survey> surveyCollection,
      QuestionService questionService,
      GridStore gridStore) {
    this.surveyCollection = surveyCollection;
    this.questionService = questionService;
    this.gridStore = gridStore;
  }

  public String create(SurveyCreateRequest request) {
    Survey survey = new Survey();
    survey.setId(KeyGenerator.next("survey"));
    survey.setActive(request.isActive());
    survey.setTitle(request.getTitle());
    survey.setDescription(request.getDescription());
    survey.setCreatedAt(new Date());
    try {
      log.info("put: {}", survey);
      surveyCollection.put(survey.getId(), survey);
    } catch (GSException e) {
      e.printStackTrace();
    }
    return survey.getId();
  }

  public boolean deleteSurvey(String surveyId) {
    try {
      return surveyCollection.remove(surveyId);
    } catch (GSException e) {
      e.printStackTrace();
      return false;
    }
  }

  public SurveyDto getSurvey(String surveyId) {
    String tql = String.format("select * from surveys where id='%s'", surveyId);
    List<SurveyDto> surveyDtos = query(tql, true);
    if (surveyDtos == null || surveyDtos.size() == 0) {
      throw new ResponseStatusException(NOT_FOUND, "Not found");
    }
    return surveyDtos.get(0);
  }

  public List<SurveyDto> getSurveys() {
    String tql = "select * from surveys limit 50";
    return query(tql, false);
  }

  private List<SurveyDto> query(String tql, boolean includeQuestion) {
    List<SurveyDto> result = new ArrayList<>();
    Query<Survey> query;
    try {
      query = surveyCollection.query(tql);
      RowSet<Survey> rs = query.fetch();
      while (rs.hasNext()) {
        Survey model = rs.next();
        List<QuestionDto> questions = new ArrayList<>();
        if (includeQuestion) questions = questionService.getQuestions(model.getId());
        result.add(
            SurveyDto.builder()
                .id(model.getId())
                .createdAt(model.getCreatedAt())
                .description(model.getDescription())
                .title(model.getTitle())
                .isActive(model.isActive())
                .questions(questions)
                .build());
      }
    } catch (GSException e) {
      e.printStackTrace();
    }
    return result;
  }
}
