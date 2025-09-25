package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.AnswerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AnswerServiceTest {

  private AnswerService answerService;
  private AnswerCollectionComponent answerCollectionComponent;
  private Map<String, Question> questionCollection;

  @BeforeEach
  void setUp() {
    answerCollectionComponent = new AnswerCollectionComponent();
    questionCollection = new HashMap<>();
    answerService = new AnswerService(answerCollectionComponent, questionCollection);

    // Add sample questions
    Question q1 = new Question();
    q1.setId("q1");
    q1.setQuestionText("What is your name?");
    q1.setPosition(1);
    q1.setSurveyId("s1");
    questionCollection.put(q1.getId(), q1);

    Question q2 = new Question();
    q2.setId("q2");
    q2.setQuestionText("What is your age?");
    q2.setPosition(2);
    q2.setSurveyId("s1");
    questionCollection.put(q2.getId(), q2);

    // Add sample answers
    Answer a1 = new Answer();
    a1.setId("a1");
    a1.setSurveyResponseId("r1");
    a1.setQuestionId("q1");
    a1.setAnswer("Alice");
    a1.setCreatedAt(new Date());
    answerCollectionComponent.addAnswer(a1.getId(), a1);

    Answer a2 = new Answer();
    a2.setId("a2");
    a2.setSurveyResponseId("r1");
    a2.setQuestionId("q2");
    a2.setAnswer("30");
    a2.setCreatedAt(new Date());
    answerCollectionComponent.addAnswer(a2.getId(), a2);

    Answer a3 = new Answer();
    a3.setId("a3");
    a3.setSurveyResponseId("r2");
    a3.setQuestionId("q1");
    a3.setAnswer("Bob");
    a3.setCreatedAt(new Date());
    answerCollectionComponent.addAnswer(a3.getId(), a3);
  }

  @Test
  void testGetAnswersByResponseId() {
    List<AnswerDto> answers = answerService.getAnswers("r1");
    assertEquals(2, answers.size());
    Set<String> answerTexts = new HashSet<>();
    for (AnswerDto dto : answers) {
      answerTexts.add(dto.getAnswer());
      assertNotNull(dto.getQuestion());
    }
    assertTrue(answerTexts.contains("Alice"));
    assertTrue(answerTexts.contains("30"));
  }

  @Test
  void testGetAnswersByQuestionId() {
    List<AnswerDto> answers = answerService.getAnswersByQuestion("q1");
    assertEquals(2, answers.size());
    Set<String> answerTexts = new HashSet<>();
    for (AnswerDto dto : answers) {
      answerTexts.add(dto.getAnswer());
      assertEquals("q1", dto.getQuestionId());
      assertNotNull(dto.getQuestion());
    }
    assertTrue(answerTexts.contains("Alice"));
    assertTrue(answerTexts.contains("Bob"));
  }
}
