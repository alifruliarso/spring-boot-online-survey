package com.galapea.techblog.springboot.onlinesurvey.client;

import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GenerateSurvey implements CommandLineRunner {

  private final GenerateSurveyService generateSurveyService;

  public GenerateSurvey(GenerateSurveyService generateSurveyService) {
    this.generateSurveyService = generateSurveyService;
  }

  @Override
  public void run(String... args) throws Exception {
    generateSurveyStackOverflow2018();
  }

  private void generateSurveyStackOverflow2018() throws IOException {
    String title = "Stack Overflow 2018 Developer Survey";
    String surveySchemaFile = "stackoverflow-2018-dev-survey_schema.csv";
    String surveyResponseFile = "stackoverflow-2018-dev-survey.csv";
    String description =
        """
            Each year, we at Stack Overflow ask the developer community about everything from their favorite technologies to their job preferences.\
            This year marks the eighth year we’ve published our Annual Developer Survey results—with the largest number of respondents yet. \
            Over 100,000 developers took the 30-minute survey in January 2018.
            """;
    String surveyId =
        generateSurveyService.generateSurveyFromFile(title, description, surveySchemaFile);
    generateSurveyService.generateAnswers(surveyId, surveyResponseFile);
  }
}
