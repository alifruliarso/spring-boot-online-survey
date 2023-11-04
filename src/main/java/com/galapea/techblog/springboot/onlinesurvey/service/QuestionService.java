package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.toshiba.mwcloud.gs.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionService {
  private Collection<String, Question> questionCollection;
  private GridStore gridStore;

  public QuestionService(Collection<String, Question> questionCollection, GridStore gridStore) {
    this.questionCollection = questionCollection;
    this.gridStore = gridStore;
  }

  public String create(QuestionCreateRequest request) {
    log.info("Create Question {}", request);
    Question question = new Question();
    question.setId(KeyGenerator.next("question"));
    question.setQuestionText(request.getQuestionText());
    question.setPosition(request.getPosition());
    question.setSurveyId(request.getSurveyId());
    try {
      questionCollection.put(question);
    } catch (GSException e) {
      e.printStackTrace();
    }
    return question.getId();
  }

  public List<QuestionDto> getQuestions(String surveyId) {
    List<QuestionDto> result = new ArrayList<>();
    Query<Question> query;
    try {
      String tql =
          String.format("select * from questions where surveyId='%s' order by position", surveyId);
      query = questionCollection.query(tql);
      RowSet<Question> rs = query.fetch();
      while (rs.hasNext()) {
        Question model = rs.next();
        result.add(
            QuestionDto.builder()
                .id(model.getId())
                .questionText(model.getQuestionText())
                .position(model.getPosition())
                .surveyId(model.getSurveyId())
                .build());
      }
    } catch (GSException e) {
      e.printStackTrace();
    }
    return result;
  }

  public QuestionDto getQuestion(String id) {
    try {
      Question model = questionCollection.get(id);
      return QuestionDto.builder()
          .id(model.getId())
          .questionText(model.getQuestionText())
          .position(model.getPosition())
          .surveyId(model.getSurveyId())
          .build();
    } catch (GSException e) {
      e.printStackTrace();
    }
    return null;
  }
}
