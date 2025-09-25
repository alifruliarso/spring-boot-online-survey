package com.galapea.techblog.springboot.onlinesurvey.client;

import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import com.galapea.techblog.springboot.onlinesurvey.service.AnswerCollectionComponent;
import com.galapea.techblog.springboot.onlinesurvey.service.QuestionService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyResponseService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GenerateSurveyServiceTest {

  private SurveyService surveyService;
  private QuestionService questionService;
  private SurveyResponseService surveyResponseService;
  private AnswerCollectionComponent answerCollectionComponent;
  private GenerateSurveyService generateSurveyService;

  @BeforeEach
  void setUp() {
    surveyService = Mockito.mock(SurveyService.class);
    questionService = Mockito.mock(QuestionService.class);
    surveyResponseService = Mockito.mock(SurveyResponseService.class);
    answerCollectionComponent = new AnswerCollectionComponent();
    generateSurveyService = new GenerateSurveyService(
        surveyService,
        questionService,
        surveyResponseService,
        answerCollectionComponent
    );
  }

  @Test
  void testGenerateSurveyFromFileCreatesSurvey() throws Exception {
    // Prepare mocks
    Mockito.when(surveyService.getSurveys()).thenReturn(Collections.emptyList());
    Mockito.when(surveyService.create(Mockito.any())).thenReturn("surveyId123");

    String tempFile = "temp_survey_schema.csv";

    String surveyId = generateSurveyService.generateSurveyFromFile(
        "UnitTestSurvey", "desc", tempFile);

    assertEquals("surveyId123", surveyId);
  }

  @Test
  void testGenerateAnswersDoesNothingIfResponsesExist() throws Exception {
    Mockito.when(surveyResponseService.getResponses(Mockito.anyString()))
        .thenReturn(Collections.singletonList(ResponseDto.builder().id("r1").build()));

    generateSurveyService.generateAnswers("surveyId123", "dummy.csv");
    // Should not throw, should not add any answers
    assertTrue(answerCollectionComponent.getAnswerCollection().isEmpty());
  }
}
