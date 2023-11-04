package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.AnswerDto;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.toshiba.mwcloud.gs.*;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerService {
  private final Collection<String, Answer> answerCollection;
  private final Collection<String, Question> questionCollection;
  private GridStore gridStore;

  public AnswerService(
      Collection<String, Answer> answerCollection,
      Collection<String, Question> questionCollection,
      GridStore gridStore) {
    this.answerCollection = answerCollection;
    this.questionCollection = questionCollection;
    this.gridStore = gridStore;
  }

  public List<AnswerDto> getAnswers(String responseId) {
    List<AnswerDto> result = new ArrayList<>();
    Query<Answer> query;
    try {
      String tql =
          String.format("select * from answers where surveyResponseId='%s' limit 100", responseId);
      queryAnswers(result, tql);
    } catch (GSException e) {
      e.printStackTrace();
    }
    return result;
  }

  public List<AnswerDto> getAnswersByQuestion(String questionId) {
    List<AnswerDto> result = new ArrayList<>();
    try {
      String tql = String.format("select * from answers where questionId='%s'", questionId);
      queryAnswers(result, tql);
    } catch (GSException e) {
      e.printStackTrace();
    }
    return result;
  }

  private void queryAnswers(List<AnswerDto> result, String tql) throws GSException {
    Query<Answer> query;
    query = answerCollection.query(tql);
    RowSet<Answer> rs = query.fetch();
    while (rs.hasNext()) {
      Answer model = rs.next();
      Question question = questionCollection.get(model.getQuestionId());
      QuestionDto questionDto =
          QuestionDto.builder()
              .id(question.getId())
              .questionText(question.getQuestionText())
              .position(question.getPosition())
              .surveyId(question.getSurveyId())
              .build();
      result.add(
          AnswerDto.builder()
              .id(model.getId())
              .surveyResponseId(model.getSurveyResponseId())
              .answer(model.getAnswer())
              .questionId(model.getQuestionId())
              .createdAt(model.getCreatedAt())
              .question(questionDto)
              .build());
    }
  }
}
