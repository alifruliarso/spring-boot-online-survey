package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.SurveyResponse;
import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SurveyResponseServiceTest {

  private SurveyResponseService surveyResponseService;
  private Map<String, SurveyResponse> surveyResponseCollection;
  private AnswerCollectionComponent answerCollectionComponent;
  private QuestionService questionService;

  @BeforeEach
  void setUp() {
    surveyResponseCollection = new HashMap<>();
    answerCollectionComponent = new AnswerCollectionComponent();
    questionService = null; // Can be mocked if needed
    surveyResponseService = new SurveyResponseService(
        surveyResponseCollection,
        answerCollectionComponent,
        questionService
    );
  }

  @Test
  void testCreateResponse() {
    ResponseDto response = surveyResponseService.createResponse("survey1");
    assertNotNull(response.getId());
    assertEquals("survey1", response.getSurveyId());
    assertNotNull(response.getStartedAt());
    assertNotNull(response.getRespondentId());
  }

  @Test
  void testSaveAndGetResponse() {
    ResponseDto responseDto = ResponseDto.builder()
        .id("resp1")
        .surveyId("survey1")
        .respondentId("user1")
        .startedAt(new Date())
        .answers(new ArrayList<>())
        .build();

    surveyResponseService.saveResponse(responseDto);

    ResponseDto fetched = surveyResponseService.getResponse("resp1");
    assertEquals("resp1", fetched.getId());
    assertEquals("survey1", fetched.getSurveyId());
    assertEquals("user1", fetched.getRespondentId());
    assertNotNull(fetched.getStartedAt());
    assertNotNull(fetched.getCompletedAt());
  }

  @Test
  void testGetResponses() {
    SurveyResponse resp1 = new SurveyResponse();
    resp1.setId("r1");
    resp1.setSurveyId("surveyA");
    resp1.setRespondentId("userA");
    resp1.setStartedAt(new Date());
    resp1.setCompletedAt(new Date());

    SurveyResponse resp2 = new SurveyResponse();
    resp2.setId("r2");
    resp2.setSurveyId("surveyA");
    resp2.setRespondentId("userB");
    resp2.setStartedAt(new Date());
    resp2.setCompletedAt(new Date());

    surveyResponseCollection.put(resp1.getId(), resp1);
    surveyResponseCollection.put(resp2.getId(), resp2);

    List<ResponseDto> responses = surveyResponseService.getResponses("surveyA");
    assertEquals(2, responses.size());
    Set<String> ids = new HashSet<>();
    for (ResponseDto dto : responses) {
      ids.add(dto.getId());
    }
    assertTrue(ids.contains("r1"));
    assertTrue(ids.contains("r2"));
  }
}
