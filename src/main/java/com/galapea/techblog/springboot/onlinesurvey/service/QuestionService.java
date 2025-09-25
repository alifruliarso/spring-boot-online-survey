package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QuestionService {
  private Map<String, Question> questionCollection;

  public QuestionService(Map<String, Question> questionCollection) {
    this.questionCollection = questionCollection;
  }

  public String create(QuestionCreateRequest request) {
    log.info("Create Question {}", request);
    Question question = new Question();
    question.setId(KeyGenerator.next("question"));
    question.setQuestionText(request.getQuestionText());
    question.setPosition(request.getPosition());
    question.setSurveyId(request.getSurveyId());
    questionCollection.put(question.getId(), question);
    return question.getId();
  }

  public List<QuestionDto> getQuestions(String surveyId) {
    List<QuestionDto> result = new ArrayList<>();
    for (Question model : questionCollection.values()) {
      if (model.getSurveyId().equals(surveyId)) {
        result.add(
            QuestionDto.builder()
                .id(model.getId())
                .questionText(model.getQuestionText())
                .position(model.getPosition())
                .surveyId(model.getSurveyId())
                .build());
      }
    }
    // Optionally sort by position
    result.sort((a, b) -> Integer.compare(a.getPosition(), b.getPosition()));
    return result;
  }

  public QuestionDto getQuestion(String id) {
    Question model = questionCollection.get(id);
    if (model == null) return null;
    return QuestionDto.builder()
        .id(model.getId())
        .questionText(model.getQuestionText())
        .position(model.getPosition())
        .surveyId(model.getSurveyId())
        .build();
  }
}
