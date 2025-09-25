package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.model.AnswerDto;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionAggregateByAnswer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AggregateService {
  private final AnswerService answerService;

  public AggregateService(AnswerService answerService) {
    this.answerService = answerService;
  }

  public List<QuestionAggregateByAnswer> getAggregateByType(String questionId) {
    List<AnswerDto> answerDtoList = answerService.getAnswersByQuestion(questionId);
    Map<String, Long> answerCountMap = new HashMap<>();
    for (AnswerDto answer : answerDtoList) {
      String answerText = answer.getAnswer();
      answerCountMap.put(answerText, answerCountMap.getOrDefault(answerText, 0L) + 1);
    }
    List<QuestionAggregateByAnswer> views = new ArrayList<>();
    for (Map.Entry<String, Long> entry : answerCountMap.entrySet()) {
      log.info("{}, count={}", entry.getKey(), entry.getValue());
      views.add(new QuestionAggregateByAnswer(entry.getValue(), entry.getKey()));
    }
    return views;
  }
}
