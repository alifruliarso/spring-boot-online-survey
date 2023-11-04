package com.galapea.techblog.springboot.onlinesurvey.config;

import com.galapea.techblog.springboot.onlinesurvey.entity.*;
import com.toshiba.mwcloud.gs.*;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridDBConfig {

  @Value("${GRIDDB_NOTIFICATION_MEMBER}")
  private String notificationMember;

  @Value("${GRIDDB_CLUSTER_NAME}")
  private String clusterName;

  @Value("${GRIDDB_USER}")
  private String user;

  @Value("${GRIDDB_PASSWORD}")
  private String password;

  @Bean
  public GridStore gridStore() throws GSException {
    // Acquiring a GridStore instance
    Properties properties = new Properties();
    properties.setProperty("notificationMember", notificationMember);
    properties.setProperty("clusterName", clusterName);
    properties.setProperty("user", user);
    properties.setProperty("password", password);
    GridStore store = GridStoreFactory.getInstance().getGridStore(properties);
    return store;
  }

  @Bean
  public Collection<String, Survey> surveyCollection(GridStore gridStore) throws GSException {
    Collection<String, Survey> collection = gridStore.putCollection("surveys", Survey.class);
    return collection;
  }

  @Bean
  public Collection<String, Respondent> respondentCollection(GridStore gridStore)
      throws GSException {
    Collection<String, Respondent> collection =
        gridStore.putCollection("respondents", Respondent.class);
    return collection;
  }

  @Bean
  public Collection<String, SurveyResponse> surveyResponseCollection(GridStore gridStore)
      throws GSException {
    Collection<String, SurveyResponse> collection =
        gridStore.putCollection("surveyResponses", SurveyResponse.class);
    collection.createIndex("surveyId");
    collection.createIndex("respondentId");
    return collection;
  }

  @Bean
  public Collection<String, Question> questionCollection(GridStore gridStore) throws GSException {
    Collection<String, Question> collection = gridStore.putCollection("questions", Question.class);
    collection.createIndex("surveyId");
    return collection;
  }

  @Bean
  public Collection<String, Answer> answerCollection(GridStore gridStore) throws GSException {
    Collection<String, Answer> urlCollection = gridStore.putCollection("answers", Answer.class);
    urlCollection.createIndex("surveyResponseId");
    return urlCollection;
  }
}
