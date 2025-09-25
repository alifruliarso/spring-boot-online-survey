package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionServiceTest {

  private QuestionService questionService;
  private Map<String, Question> questionCollection;

  @BeforeEach
  void setUp() {
    questionCollection = new HashMap<>();
    questionService = new QuestionService(questionCollection);
  }

  @Test
  void testCreateAndGetQuestion() {
    QuestionCreateRequest req = new QuestionCreateRequest();
    req.setSurveyId("survey1");
    req.setQuestionText("What is your favorite color?");
    req.setPosition(1);

    String questionId = questionService.create(req);
    assertNotNull(questionId);

    QuestionDto dto = questionService.getQuestion(questionId);
    assertNotNull(dto);
    assertEquals("survey1", dto.getSurveyId());
    assertEquals("What is your favorite color?", dto.getQuestionText());
    assertEquals(1, dto.getPosition());
  }

  @Test
  void testGetQuestionsBySurveyId() {
    QuestionCreateRequest req1 = new QuestionCreateRequest();
    req1.setSurveyId("surveyA");
    req1.setQuestionText("Q1");
    req1.setPosition(1);

    QuestionCreateRequest req2 = new QuestionCreateRequest();
    req2.setSurveyId("surveyA");
    req2.setQuestionText("Q2");
    req2.setPosition(2);

    questionService.create(req1);
    questionService.create(req2);

    List<QuestionDto> questions = questionService.getQuestions("surveyA");
    assertEquals(2, questions.size());
    assertEquals("Q1", questions.get(0).getQuestionText());
    assertEquals("Q2", questions.get(1).getQuestionText());
  }
}
