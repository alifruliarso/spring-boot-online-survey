package com.galapea.techblog.springboot.onlinesurvey.client;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Survey;
import com.galapea.techblog.springboot.onlinesurvey.entity.SurveyResponse;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.galapea.techblog.springboot.onlinesurvey.model.SurveyCreateRequest;
import com.galapea.techblog.springboot.onlinesurvey.service.KeyGenerator;
import com.galapea.techblog.springboot.onlinesurvey.service.QuestionService;
import com.galapea.techblog.springboot.onlinesurvey.service.SurveyService;
import com.toshiba.mwcloud.gs.Collection;
import com.toshiba.mwcloud.gs.GSException;
import com.toshiba.mwcloud.gs.Query;
import com.toshiba.mwcloud.gs.RowSet;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GenerateSurveyService {
  private final SurveyService surveyService;
  private final QuestionService questionService;
  private final Collection<String, Survey> surveyCollection;
  private final Collection<String, SurveyResponse> surveyResponseCollection;
  private final Collection<String, Answer> answerCollection;

  public GenerateSurveyService(
      SurveyService surveyService,
      QuestionService questionService,
      Collection<String, Survey> surveyCollection,
      Collection<String, SurveyResponse> surveyResponseCollection,
      Collection<String, Answer> answerCollection) {
    this.surveyService = surveyService;
    this.questionService = questionService;
    this.surveyCollection = surveyCollection;
    this.surveyResponseCollection = surveyResponseCollection;
    this.answerCollection = answerCollection;
  }

  private Survey findSurveyByTitle(String title) throws GSException {
    String tql = String.format("select * from surveys where title='%s'", title);
    Query<Survey> query = surveyCollection.query(tql);
    RowSet<Survey> rs = query.fetch();
    if (rs.hasNext()) {
      return rs.next();
    }
    return null;
  }

  public String generateSurveyFromFile(String title, String description, String surveySchemaFile)
      throws IOException {
    Survey survey = findSurveyByTitle(title);
    if (survey != null) {
      log.info("Example survey already exists. Title: {}", title);
      return survey.getId();
    }

    log.info("Let me create survey: {}", title);
    SurveyCreateRequest request = new SurveyCreateRequest();
    request.setActive(true);
    request.setTitle(title);

    request.setDescription(description);
    String surveyId = surveyService.create(request);
    log.info("Created surveyId: {}", surveyId);

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
    log.info("Inserted {} questions", lineNumber);
    br.close();
    return surveyId;
  }

  private SurveyResponse findSurveyResponse(String surveyId) throws GSException {
    String tql = String.format("select * from surveyResponses where surveyId='%s'", surveyId);
    Query<SurveyResponse> query = surveyResponseCollection.query(tql);
    RowSet<SurveyResponse> rs = query.fetch();
    if (rs.hasNext()) {
      return rs.next();
    }
    return null;
  }

  public void generateAnswers(String surveyId, String surveyResponseFile) throws IOException {
    if (findSurveyResponse(surveyId) != null) {
      log.info("Not generating answers from example.");
      return;
    }
    List<QuestionDto> questions = questionService.getQuestions(surveyId);

    Resource resource = new ClassPathResource(surveyResponseFile);
    Path path = Path.of(resource.getURI());
    CsvReader csvReader = CsvReader.builder().fieldSeparator(',').quoteCharacter('"').build(path);
    long csvRowCount =
        CsvReader.builder().fieldSeparator(',').quoteCharacter('"').build(path).stream().count();
    log.info("Generating {} answers", csvRowCount);
    for (final Iterator<CsvRow> iterator = csvReader.iterator(); iterator.hasNext(); ) {
      final CsvRow csvRow = iterator.next();
      List<String> rowFields = csvRow.getFields();
      SurveyResponse newResponse = new SurveyResponse();
      newResponse.setId(KeyGenerator.next("response"));
      newResponse.setSurveyId(surveyId);
      newResponse.setStartedAt(new Date());
      var respondentId = KeyGenerator.next("respondent");
      newResponse.setRespondentId(respondentId);
      surveyResponseCollection.setAutoCommit(false);
      surveyResponseCollection.put(newResponse);
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
      answerCollection.put(answerList);
      //      randomDelay();
      SurveyResponse updateResponse = surveyResponseCollection.get(newResponse.getId(), true);
      updateResponse.setCompletedAt(new Date());
      surveyResponseCollection.put(updateResponse.getId(), updateResponse);
      surveyResponseCollection.commit();
    }
    log.info("Finished insert answers");
  }

  void randomDelay() {
    int min = 1000;
    int max = 4000;
    try {
      TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(min, max));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
