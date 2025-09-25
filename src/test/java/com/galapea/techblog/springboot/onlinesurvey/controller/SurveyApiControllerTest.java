package com.galapea.techblog.springboot.onlinesurvey.controller;

import com.galapea.techblog.springboot.onlinesurvey.service.SurveyResponseService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SurveyApiController.class)
class SurveyApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SurveyService surveyService;

  @MockitoBean
  private SurveyResponseService surveyResponseService;

  @BeforeEach
  void setUp() {
    Mockito.when(surveyService.getSurveys()).thenReturn(Collections.emptyList());
    Mockito.when(surveyService.getSurvey(Mockito.anyString())).thenReturn(null);
    Mockito.when(surveyService.create(Mockito.any())).thenReturn("survey123");
    Mockito.when(surveyResponseService.getResponses(Mockito.anyString())).thenReturn(Collections.emptyList());
  }

  @Test
  void testGetSurveys() throws Exception {
    mockMvc.perform(get("/api/surveys"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  void testGetSurvey() throws Exception {
    mockMvc.perform(get("/api/surveys/survey1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testCreateSurvey() throws Exception {
    String json = "{\"title\":\"Test Survey\",\"description\":\"desc\",\"active\":true}";
    mockMvc.perform(post("/api/surveys")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value("survey123"));
  }

  @Test
  void testGetSurveyResponse() throws Exception {
    mockMvc.perform(get("/api/surveys/survey1/response"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray());
  }
}
