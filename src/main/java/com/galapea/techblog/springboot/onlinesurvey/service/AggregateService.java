package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import com.galapea.techblog.springboot.onlinesurvey.entity.Question;
import com.galapea.techblog.springboot.onlinesurvey.model.AnswerDto;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionAggregateByAnswer;
import com.toshiba.mwcloud.gs.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AggregateService {
  private final GridStore gridStore;
  private final Collection<String, Answer> answerCollection;
  private final Collection<String, Question> questionCollection;
  private final QuestionService questionService;
  private final AnswerService answerService;

  public AggregateService(
      GridStore gridStore,
      Collection<String, Answer> answerCollection,
      Collection<String, Question> questionCollection,
      QuestionService questionService,
      AnswerService answerService) {
    this.gridStore = gridStore;
    this.answerCollection = answerCollection;
    this.questionCollection = questionCollection;
    this.questionService = questionService;
    this.answerService = answerService;
  }

  public List<QuestionAggregateByAnswer> getAggregateByType(String questionId) {
    List<AnswerDto> answerDtoList = answerService.getAnswersByQuestion(questionId);
    Set<String> uniqueAnswer = new HashSet<>();
    for (AnswerDto answer : answerDtoList) {
      uniqueAnswer.add(answer.getAnswer());
    }
    List<QuestionAggregateByAnswer> views = new ArrayList<>();
    try {
      for (String answerText : uniqueAnswer) {
        Container<?, Row> container = gridStore.getContainer("answers");
        Query<AggregationResult> query =
            container.query(
                "SELECT COUNT(*) from answers WHERE answer='" + answerText + "'",
                AggregationResult.class);
        RowSet<AggregationResult> rs = query.fetch();
        if (rs.hasNext()) {
          AggregationResult row = rs.next();
          Long count = row.getLong();
          log.info("{}, count={}", answerText, count);
          views.add(new QuestionAggregateByAnswer(count, answerText));
        }
      }
    } catch (GSException e) {
      e.printStackTrace();
    }
    return views;
  }
}
