package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.AnswerDto;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnswerService {
  private final AnswerCollectionComponent answerCollectionComponent;
  private final Map<String, Question> questionCollection;

  public AnswerService(
      AnswerCollectionComponent answerCollectionComponent,
      Map<String, Question> questionCollection) {
    this.answerCollectionComponent = answerCollectionComponent;
    this.questionCollection = questionCollection;
  }

  public List<AnswerDto> getAnswers(String responseId) {
    List<AnswerDto> result = new ArrayList<>();
    for (Answer model : answerCollectionComponent.getAnswerCollection().values()) {
      if (model.getSurveyResponseId().equals(responseId)) {
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
    // Optionally limit to 100
    if (result.size() > 100) {
      return result.subList(0, 100);
    }
    return result;
  }

  public List<AnswerDto> getAnswersByQuestion(String questionId) {
    List<AnswerDto> result = new ArrayList<>();
    for (Answer model : answerCollectionComponent.getAnswerCollection().values()) {
      if (model.getQuestionId().equals(questionId)) {
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
    return result;
  }
}
