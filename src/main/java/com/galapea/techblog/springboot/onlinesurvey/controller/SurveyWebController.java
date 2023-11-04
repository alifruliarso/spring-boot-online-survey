package com.galapea.techblog.springboot.onlinesurvey.controller;

import com.galapea.techblog.springboot.onlinesurvey.model.AnswerQuestion;
import com.galapea.techblog.springboot.onlinesurvey.model.QuestionDto;
import com.galapea.techblog.springboot.onlinesurvey.model.ResponseDto;
import com.galapea.techblog.springboot.onlinesurvey.service.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SurveyWebController {
  private final SurveyService surveyService;
  private final SurveyResponseService surveyResponseService;
  private final AnswerService answerService;
  private final QuestionService questionService;
  private final AggregateService aggregateService;

  public SurveyWebController(
      SurveyService surveyService,
      SurveyResponseService surveyResponseService,
      AnswerService answerService,
      QuestionService questionService,
      AggregateService aggregateService) {
    this.surveyService = surveyService;
    this.surveyResponseService = surveyResponseService;
    this.answerService = answerService;
    this.questionService = questionService;
    this.aggregateService = aggregateService;
  }

  @GetMapping("/surveys")
  public String listSurveys(Model model) {
    model.addAttribute("surveys", surveyService.getSurveys());
    return "surveys";
  }

  @GetMapping("/surveys/{surveyId}/responses")
  public String showResponse(@PathVariable("surveyId") String surveyId, Model model) {
    model.addAttribute("responses", surveyResponseService.getResponses(surveyId));
    model.addAttribute("survey", surveyService.getSurvey(surveyId));
    return "responses";
  }

  @GetMapping("/answers/{responseId}")
  public String showAnswer(@PathVariable("responseId") String responseId, Model model) {
    model.addAttribute("answers", answerService.getAnswers(responseId));
    ResponseDto responseDto = surveyResponseService.getResponse(responseId);
    model.addAttribute("survey", surveyService.getSurvey(responseDto.getSurveyId()));
    return "answers";
  }

  @GetMapping("/responses/{surveyId}")
  public String addNewResponse(@PathVariable("surveyId") String surveyId, Model model) {
    ResponseDto responseDto = surveyResponseService.createResponse(surveyId);
    List<QuestionDto> questions = questionService.getQuestions(surveyId);
    List<AnswerQuestion> answers = new ArrayList<>(questions.size());
    for (QuestionDto question : questions) {
      AnswerQuestion answerQuestion = new AnswerQuestion();
      answerQuestion.setQuestionText(question.getQuestionText());
      answerQuestion.setPosition(question.getPosition());
      answerQuestion.setQuestionId(question.getId());
      answers.add(answerQuestion);
    }
    responseDto.setAnswers(answers);
    model.addAttribute("questions", questions);
    model.addAttribute("survey", surveyService.getSurvey(surveyId));
    model.addAttribute("response", responseDto);
    return "add-answer";
  }

  @PostMapping("/answers")
  public String saveResponse(ResponseDto responseDto, Model model) {
    surveyResponseService.saveResponse(responseDto);
    model.addAttribute("successMessage", "Answer saved successfully!");
    return "redirect:/surveys";
  }

  @GetMapping("/chart/{questionId}")
  public String showAggregate(@PathVariable("questionId") String questionId, Model model) {
    model.addAttribute("aggregates1", aggregateService.getAggregateByType(questionId));
    QuestionDto question = questionService.getQuestion(questionId);
    model.addAttribute("aggregates1_label", question.getQuestionText());
    model.addAttribute("question", question);
    return "chart";
  }
}
