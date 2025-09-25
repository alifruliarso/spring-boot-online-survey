package com.galapea.techblog.springboot.onlinesurvey.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.SurveyResponse;
import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class SurveyResponseService {
  private final Map<String, SurveyResponse> surveyResponseCollection;
  private final AnswerCollectionComponent answerCollectionComponent;
  private QuestionService questionService;

  public SurveyResponseService(
      Map<String, SurveyResponse> surveyResponseCollection,
      AnswerCollectionComponent answerCollectionComponent,
      QuestionService questionService) {
    this.surveyResponseCollection = surveyResponseCollection;
    this.answerCollectionComponent = answerCollectionComponent;
    this.questionService = questionService;
  }

  public ResponseDto createResponse(String surveyId) {
    return ResponseDto.builder()
        .id(KeyGenerator.next("response"))
        .surveyId(surveyId)
        .startedAt(new Date())
        .respondentId(KeyGenerator.next("respondent"))
        .build();
  }

  public ResponseDto getResponse(String id) {
    SurveyResponse response = surveyResponseCollection.get(id);
    if (response == null) {
      throw new ResponseStatusException(NOT_FOUND, "Not found");
    }
    return ResponseDto.builder()
        .id(response.getId())
        .surveyId(response.getSurveyId())
        .respondentId(response.getRespondentId())
        .startedAt(response.getStartedAt())
        .completedAt(response.getCompletedAt())
        .build();
  }

  public List<ResponseDto> getResponses(String surveyId) {
    List<ResponseDto> result = new ArrayList<>();
    for (SurveyResponse model : surveyResponseCollection.values()) {
      if (model.getSurveyId().equals(surveyId)) {
        result.add(
            ResponseDto.builder()
                .id(model.getId())
                .surveyId(model.getSurveyId())
                .respondentId(model.getRespondentId())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .build());
      }
    }
    // Optionally sort by startedAt desc and limit to 100
    result.sort((a, b) -> b.getStartedAt().compareTo(a.getStartedAt()));
    if (result.size() > 100) {
      return result.subList(0, 100);
    }
    return result;
  }

  public void saveResponse(ResponseDto responseDto) {
    responseDto.getAnswers().stream().forEach(r -> log.info(String.valueOf(r)));
    log.info("{}", responseDto);
    SurveyResponse newResponse = new SurveyResponse();
    newResponse.setId(responseDto.getId());
    newResponse.setSurveyId(responseDto.getSurveyId());
    newResponse.setStartedAt(responseDto.getStartedAt());
    newResponse.setRespondentId(responseDto.getRespondentId());
    surveyResponseCollection.put(newResponse.getId(), newResponse);
    for (int i = 0; i < responseDto.getAnswers().size(); i++) {
      Answer answer = new Answer();
      answer.setId(KeyGenerator.next("answer"));
      answer.setSurveyResponseId(newResponse.getId());
      answer.setCreatedAt(new Date());
      answer.setQuestionId(responseDto.getAnswers().get(i).getQuestionId());
      answer.setAnswer(responseDto.getAnswers().get(i).getAnswerText());
      answerCollectionComponent.addAnswer(answer.getId(), answer);
    }
    // Update completedAt
    SurveyResponse updateResponse = surveyResponseCollection.get(newResponse.getId());
    updateResponse.setCompletedAt(new Date());
    surveyResponseCollection.put(updateResponse.getId(), updateResponse);
  }

  public void addSurveyResponse(SurveyResponse response) {
    surveyResponseCollection.put(response.getId(), response);
  }

  public void updateSurveyResponse(SurveyResponse response) {
    surveyResponseCollection.put(response.getId(), response);
  }
}
