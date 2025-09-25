package com.galapea.techblog.springboot.onlinesurvey.client;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Survey;
import com.galapea.techblog.springboot.onlinesurvey.entity.SurveyResponse;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.service.AnswerCollectionComponent;
import com.galapea.techblog.springboot.onlinesurvey.service.KeyGenerator;
import com.galapea.techblog.springboot.onlinesurvey.service.QuestionService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyResponseService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyService;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class GenerateSurveyService {
  private final SurveyService surveyService;
  private final QuestionService questionService;
  private final SurveyResponseService surveyResponseService;
  private final AnswerCollectionComponent answerCollectionComponent;

  public GenerateSurveyService(
      SurveyService surveyService,
      QuestionService questionService,
      SurveyResponseService surveyResponseService,
      AnswerCollectionComponent answerCollectionComponent) {
    this.surveyService = surveyService;
    this.questionService = questionService;
    this.surveyResponseService = surveyResponseService;
    this.answerCollectionComponent = answerCollectionComponent;
  }

  private Survey findSurveyByTitle(String title) {
    return surveyService.getSurveys().stream()
        .filter(s -> s.getTitle().equals(title))
        .findFirst()
        .map(
            surveyDto -> {
              Survey survey = new Survey();
              survey.setId(surveyDto.getId());
              survey.setTitle(surveyDto.getTitle());
              survey.setDescription(surveyDto.getDescription());
              survey.setCreatedAt(surveyDto.getCreatedAt());
              survey.setActive(surveyDto.isActive());
              return survey;
            })
        .orElse(null);
  }

  public String generateSurveyFromFile(String title, String description, String surveySchemaFile)
      throws IOException {
    Survey survey = findSurveyByTitle(title);
    if (survey != null) {
      return survey.getId();
    }

    SurveyCreateRequest request = new SurveyCreateRequest();
    request.setActive(true);
    request.setTitle(title);

    request.setDescription(description);
    String surveyId = surveyService.create(request);

    Resource resource = new ClassPathResource(surveySchemaFile);
    FileReader fr = new FileReader(resource.getFile());
    BufferedReader br = new BufferedReader(fr);
    String line = br.readLine(); // Reading header, Ignoring
    br.readLine(); // skip
    int lineNumber = 1;
    while ((line = br.readLine()) != null && !line.isEmpty()) {
      String[] fields = line.split(",", 2);
      String questionText = fields[1];
      QuestionCreateRequest question = new QuestionCreateRequest();
      question.setSurveyId(surveyId);
      question.setQuestionText(questionText);
      question.setPosition(lineNumber);
      questionService.create(question);
      lineNumber++;
    }
    br.close();
    return surveyId;
  }

  private SurveyResponse findSurveyResponse(String surveyId) {
    List<ResponseDto> responses = surveyResponseService.getResponses(surveyId);
    if (responses != null && !responses.isEmpty()) {
      return new SurveyResponse();
    }
    return null;
  }

  public void generateAnswers(String surveyId, String surveyResponseFile) throws IOException {
    if (findSurveyResponse(surveyId) != null) {
      return;
    }
    List<QuestionDto> questions = questionService.getQuestions(surveyId);

    Resource resource = new ClassPathResource(surveyResponseFile);
    Path path = Path.of(resource.getURI());
    try (CsvReader<CsvRecord> csvReader =
        CsvReader.builder().fieldSeparator(',').quoteCharacter('"').ofCsvRecord(path)) {
      for (final Iterator<CsvRecord> iterator = csvReader.iterator(); iterator.hasNext(); ) {
        final CsvRecord csvRow = iterator.next();
        List<String> rowFields = csvRow.getFields();
        SurveyResponse newResponse = new SurveyResponse();
        newResponse.setId(KeyGenerator.next("response"));
        newResponse.setSurveyId(surveyId);
        newResponse.setStartedAt(new Date());
        var respondentId = KeyGenerator.next("respondent");
        newResponse.setRespondentId(respondentId);
        surveyResponseService.addSurveyResponse(newResponse);
        List<Answer> answerList = new ArrayList<>(questions.size());
        for (int i = 0; i < questions.size(); i++) {
          Answer answer = new Answer();
          answer.setId(KeyGenerator.next("answer"));
          answer.setSurveyResponseId(newResponse.getId());
          answer.setCreatedAt(new Date());
          answer.setQuestionId(questions.get(i).getId());
          answer.setAnswer(rowFields.get(i + 1));
          answerList.add(answer);
        }
        for (Answer answer : answerList) {
          answerCollectionComponent.addAnswer(answer.getId(), answer);
        }
        newResponse.setCompletedAt(new Date());
        surveyResponseService.updateSurveyResponse(newResponse);
      }
    }
  }
}
