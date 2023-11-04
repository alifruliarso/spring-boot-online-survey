package com.galapea.techblog.springboot.onlinesurvey.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Survey;
import com.galapea.techblog.springboot.onlinesurvey.entity.SurveyResponse;
import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import com.toshiba.mwcloud.gs.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class SurveyResponseService {
  private final Collection<String, SurveyResponse> surveyResponseCollection;
  private final Collection<String, Survey> surveyCollection;
  private final Collection<String, Answer> answerCollection;
  private QuestionService questionService;
  private GridStore gridStore;

  public SurveyResponseService(
      Collection<String, SurveyResponse> surveyResponseCollection,
      Collection<String, Survey> surveyCollection,
      Collection<String, Answer> answerCollection,
      QuestionService questionService,
      GridStore gridStore) {
    this.surveyResponseCollection = surveyResponseCollection;
    this.surveyCollection = surveyCollection;
    this.answerCollection = answerCollection;
    this.questionService = questionService;
    this.gridStore = gridStore;
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
    SurveyResponse response = null;
    try {
      response = surveyResponseCollection.get(id);
    } catch (GSException e) {
      e.printStackTrace();
    }
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
    Query<SurveyResponse> query;
    try {
      String tql =
          String.format(
              "select * from surveyResponses where surveyId='%s' order by startedAt desc limit 100",
              surveyId);
      log.info("tql:{}", tql);
      query = surveyResponseCollection.query(tql);
      RowSet<SurveyResponse> rs = query.fetch();
      while (rs.hasNext()) {
        SurveyResponse model = rs.next();
        result.add(
            ResponseDto.builder()
                .id(model.getId())
                .surveyId(model.getSurveyId())
                .respondentId(model.getRespondentId())
                .startedAt(model.getStartedAt())
                .completedAt(model.getCompletedAt())
                .build());
      }
    } catch (GSException e) {
      e.printStackTrace();
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
    try {
      surveyResponseCollection.setAutoCommit(false);
      surveyResponseCollection.put(newResponse);
      List<Answer> answerList = new ArrayList<>(responseDto.getAnswers().size());
      for (int i = 0; i < responseDto.getAnswers().size(); i++) {
        Answer answer = new Answer();
        answer.setId(KeyGenerator.next("answer"));
        answer.setSurveyResponseId(newResponse.getId());
        answer.setCreatedAt(new Date());
        answer.setQuestionId(responseDto.getAnswers().get(i).getQuestionId());
        answer.setAnswer(responseDto.getAnswers().get(i).getAnswerText());
        answerList.add(answer);
      }
      answerCollection.put(answerList);
      SurveyResponse updateResponse = surveyResponseCollection.get(newResponse.getId(), true);
      updateResponse.setCompletedAt(new Date());
      surveyResponseCollection.put(updateResponse.getId(), updateResponse);
      surveyResponseCollection.commit();
    } catch (GSException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
