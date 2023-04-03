package huhtala.bryce.jpgpt.controller;

import com.google.gson.Gson;
import huhtala.bryce.jpgpt.dao.LanguageDataDao;
import huhtala.bryce.jpgpt.model.*;
import huhtala.bryce.jpgpt.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/")
public class LanguageDataController {

    @Autowired
    private final LanguageDataDao languageDataDao;

    @Autowired
    private final ChatGptService chatGptService;

    public LanguageDataController(LanguageDataDao languageDataDao, ChatGptService chatGptService) {
        this.languageDataDao = languageDataDao;
        this.chatGptService = chatGptService;
    }

//    @GetMapping("/sets/{id}")
//    public Set getSetById(@PathVariable("id") int id) {
//        return languageDataDao.getSetById(id);
//    }

    @CrossOrigin(origins = "*")
    @PostMapping("/kanji/{id}")
    public QuizEvaluation judgeItemAnswerById(@PathVariable("id") int id, @RequestParam String answer, @RequestParam(required = false) boolean appeal) {

        Kanji kanji = languageDataDao.getKanjiById(id);

        if (!appeal) {
            String[] correctAnswers = kanji.getMeanings().split("\\|");

            for (String correctAnswer : correctAnswers) {
                if (answer.equalsIgnoreCase(correctAnswer))
                    return new QuizEvaluation("Correct");
            }

            return new QuizEvaluation("Incorrect");
        } else {
            if (runKanjiAppeal(kanji.getKanji(), answer))
                return new QuizEvaluation("Correct");
            else
                return new QuizEvaluation("Incorrect");
        }


//        if (evaluation.isInputWrittenWithEnglish()) {
//            if (evaluation.isInputCorrectAnswer())
//                return new QuizEvaluation("Correct");
//            else
//                return new QuizEvaluation("Incorrect");
//        }
//        else
//            return new QuizEvaluation("LanguageError");
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/kanji/{id}/meaning")
    public String[] judgeItemAnswerById(@PathVariable("id") int id) {

        Kanji kanji = languageDataDao.getKanjiById(id);

        String[] correctAnswers = kanji.getMeanings().split("\\|");

        return correctAnswers;

    }


    @CrossOrigin(origins = "*")
    @GetMapping("/sets/{id}/random")
    public KanjiQuiz getRandomItemFromSet(@PathVariable("id") int id) {
        return new KanjiQuiz(languageDataDao.getRandomKanji());
    }


    private boolean runKanjiAppeal(String kanji, String answer) {
        BotRequest botRequest = new BotRequest();
        botRequest.setMessage("{ kanji: \"" + kanji + "\", question: \"What does this kanji mean?\", userInput: \"" + answer + "\"\n" +
                "Respond with JSON in the following format in less than 20 words:\n" +
                "{ isInputCorrectAnswer: \"true|false\" }");
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null);
        String json = response.getChoices().get(0).getMessage().getContent();
        System.out.println(json);
        Gson gson = new Gson();
        ChatGptEvaluation evaluation = gson.fromJson(json, ChatGptEvaluation.class);

        return evaluation.isInputCorrectAnswer();
    }
}


//    @CrossOrigin(origins = "*")
//    @PostMapping("/item/{id}")
//    public QuizEvaluation judgeItemAnswerById(@PathVariable("id") int id, @RequestParam String answer) {
//        Kanji kanji = languageDataDao.getItemById(id);
//        BotRequest botRequest = new BotRequest();
//        botRequest.setMessage("{ promptInJapanese: \"このかんじのいみはなんですか？\", promptInEnglish: \"What does this kanji mean?\", theKanji: \"" + kanji.getKanji() + "\", input = \"" + answer + "\" }\n" +
//                "Respond with JSON in the following format in less than 20 words:\n" +
//                "{ isInputWrittenWithEnglish: \"true|false\", isInputCorrectAnswer: \"true|false\" }");
//        ChatGptResponse response = chatGptService.askQuestion(botRequest, null);
//        String json = response.getChoices().get(0).getMessage().getContent();
//        System.out.println(json);
//        Gson gson = new Gson();
//        ChatGptEvaluation evaluation = gson.fromJson(json, ChatGptEvaluation.class);
////        System.out.println("isInputCorrectAnswer" + evaluation.isInputCorrectAnswer());
////        System.out.println("isInputWrittenInEnglish " + evaluation.isInputWrittenWithEnglish());
//        if (evaluation.isInputWrittenWithEnglish()) {
//            if (evaluation.isInputCorrectAnswer())
//                return new QuizEvaluation("Correct");
//            else
//                return new QuizEvaluation("Incorrect");
//        }
//        else
//            return new QuizEvaluation("LanguageError");
//    }
