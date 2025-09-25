package com.galapea.techblog.springboot.onlinesurvey.service;

import com.galapea.techblog.springboot.onlinesurvey.entity.Answer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class AnswerCollectionComponent {
  private final Map<String, Answer> answerCollection = new ConcurrentHashMap<>();

  public Map<String, Answer> getAnswerCollection() {
    return answerCollection;
  }

  public void addAnswer(String id, Answer answer) {
    answerCollection.put(id, answer);
  }

  public Answer removeAnswer(String id) {
    return answerCollection.remove(id);
  }
}
