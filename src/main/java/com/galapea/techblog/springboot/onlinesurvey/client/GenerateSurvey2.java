package com.galapea.techblog.springboot.onlinesurvey.client;

import java.io.IOException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GenerateSurvey2 implements CommandLineRunner {
  private final GenerateSurveyService generateSurveyService;

  public GenerateSurvey2(GenerateSurveyService generateSurveyService) {
    this.generateSurveyService = generateSurveyService;
  }

  @Override
  public void run(String... args) throws Exception {
    generateSurveyHackerNewsSalarySurvey();
  }

  private void generateSurveyHackerNewsSalarySurvey() throws IOException {
    String title = "2016 Hacker News Salary Survey";
    String surveySchemaFile = "2016HackerNewsSalarySurvey_schema.csv";
    String surveyResponseFile = "2016HackerNewsSalarySurvey_clean.csv";
    String description =
        """
                        Results from a 2016 survey of Hacker News users about salary and benefits.
                    """;
    String surveyId =
        generateSurveyService.generateSurveyFromFile(title, description, surveySchemaFile);
    generateSurveyService.generateAnswers(surveyId, surveyResponseFile);
  }
}
