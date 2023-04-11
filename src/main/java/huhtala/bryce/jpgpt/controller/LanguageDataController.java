package huhtala.bryce.jpgpt.controller;

import com.google.gson.Gson;
import huhtala.bryce.jpgpt.dao.LanguageDataDao;
import huhtala.bryce.jpgpt.model.*;
import huhtala.bryce.jpgpt.service.ChatGptService;
import huhtala.bryce.jpgpt.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@CrossOrigin
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

    @PostMapping("/kanji/{id}")
    public QuizEvaluation judgeItemAnswerById(@PathVariable("id") int id, @RequestParam String answer, @RequestParam(required = false) boolean appeal) {

//        Item item = languageDataDao.getKanjiById(id);

        if (!appeal) {
//            String[] correctAnswers = item.getMeanings().split("\\|");

//            for (String correctAnswer : correctAnswers) {
//                if (answer.equalsIgnoreCase(correctAnswer))
                    return new QuizEvaluation("Correct");
            }

//            return new QuizEvaluation("Incorrect");
//        } else {
//            if (runKanjiAppeal(kanji.getKanji(), answer))
//                return new QuizEvaluation("Correct");
//            else
                return new QuizEvaluation("Incorrect");
//        }

    }

    @PostMapping("/question/{id}")
    public QuestionEvaluation judgeQuestionAnswerById(@PathVariable("id") int id, @RequestParam String answer, @RequestParam(required = false) boolean appeal) {

        Question question = languageDataDao.getQuestionById(id);

            return  (evaluateQuestionResponse(question, answer));
    }



    @GetMapping("/kanji/{id}/meaning")
    public String[] judgeItemAnswerById(@PathVariable("id") int id) {

//        Kanji kanji = languageDataDao.getKanjiById(id);
//
//        String[] correctAnswers = kanji.getMeanings().split("\\|");
//
//        return correctAnswers;
        return null;
    }


    @GetMapping("/item")
    public Quiz getChallengeItem() {
        Item item = languageDataDao.getChallengeItem();

        Quiz quiz = new Quiz();

        boolean japaneseContent = true;

        quiz.setId(item.getItemId());
        quiz.setTopic(item.getItemCategoryName());
        Random random = new Random();

        switch (item.getItemCategoryName()) {
            case "Kanji":
                switch(random.nextInt(3)) {
                    case 0:
                        quiz.setType("meaning");
                        break;
                    case 1:
                        quiz.setType("kunreading");
                        break;
                    case 2:
                        quiz.setType("onreading");
                        break;
                }
                break;
            case "Vocab":
                switch(random.nextInt(2)) {
                    case 0:
                        quiz.setType("j2e");
                        break;
                    case 1:
                        quiz.setType("e2j");
                        japaneseContent = false;
                        break;
                }
                break;
            case "Conversation":
                switch(random.nextInt(2 + (item.isQuestionCandidate() ? 1 : 0))) {
                    case 0:
                        quiz.setType("j2e");
                        break;
                    case 1:
                        quiz.setType("e2j");
                        japaneseContent = false;
                        break;
                    case 2:
                        quiz.setType("question");
                        break;
                }
                break;

        }

        if (japaneseContent) {
            quiz.setContent(item.getText());
            if (item.getText().equals("NULL") || item.getText().equals("")) {
                quiz.setContent(item.getHiragana());
            } else {
                if (quiz.getType().equalsIgnoreCase("j2e") || quiz.getType().equalsIgnoreCase("question"))
                    quiz.setAltContent(item.getHiragana());
            }
        } else {
            String[] meanings = item.getMeaning().split("\\|");
            quiz.setContent(meanings[0]);
        }


        return quiz;
    }


    @PostMapping("/item/{id}")
    public boolean checkSubmission(@PathVariable("id") int id, @RequestParam String input, @RequestParam String type ) {

        Item item = languageDataDao.getItemById(id);

        switch (type) {
            case "j2e":
            case "meaning":
                String[] meanings = item.getMeaning().split("\\|");
                for (String meaning : meanings) {
                    if (input.equalsIgnoreCase(meaning))
                        return true;
                }
                return PromptService.checkMeaning(chatGptService, item.getText(), input);
            case "e2j":
                if (item.getText().equalsIgnoreCase(input)) return true;
                if (item.getHiragana().equalsIgnoreCase(input)) return true;

                return PromptService.checkJapaneseTranslation(chatGptService, item.getText(), input);
            case "kunreading": {
                String[] readingTypes = item.getHiragana().split("\\;");
                String[] onReadings = readingTypes[0].split("\\|");

                for (String onReading : onReadings) {
                    if (input
                            .replace(".", "")
                            .replace("-", "")
                            .equalsIgnoreCase(onReading))
                        return true;
                }
            }
            case "onreading": {
                String[] readingTypes = item.getHiragana().split("\\;");
                String[] kunReadings = readingTypes[1].split("\\|");

                for (String kunReading : kunReadings) {
                    if (input
                            .replace(".", "")
                            .replace("-", "")
                            .equalsIgnoreCase(kunReading))
                        return true;
                }
            }
        }

        return false;
    }


    @GetMapping("/item/{id}/hiragana")
    public String[] getAnswer(@PathVariable("id") int id, @RequestParam String type ) {

        Item item = languageDataDao.getItemById(id);

        if (type.equalsIgnoreCase("j2e") || type.equalsIgnoreCase("question")) {
            return item.getHiragana().split("\\;");
        } else return new String[]{""};
    }

    @GetMapping("/item/{id}/answer")
    public String[] getHiragana(@PathVariable("id") int id, @RequestParam String type ) {

        Item item = languageDataDao.getItemById(id);

        switch (type) {
            case "j2e":
            case "meaning":
                return item.getMeaning().split("\\|");
            case "e2j":
                return new String[]{item.getHiragana()};
            case "kunreading": {
                String[] readingTypes = item.getHiragana().split("\\;");
                String[] kunreadings = readingTypes[0].split("\\|");
                for (String kunreading : kunreadings) {
                    kunreading = kunreading
                            .replace(".", "")
                            .replace("-", "");
                }
                return kunreadings;
            }
            case "onreading": {
                String[] readingTypes = item.getHiragana().split("\\;");
                String[] onreadings = readingTypes[1].split("\\|");
                for (String onreading : onreadings) {
                    onreading = onreading
                            .replace(".", "")
                            .replace("-", "");
                }
                return onreadings;
            }
        }
        return new String[]{""};
    }



    private boolean runKanjiAppeal(String kanji, String answer) {
        BotRequest botRequest = new BotRequest();
        botRequest.setMessage("{ kanji: \"" + kanji + "\", question: \"What does this kanji mean?\", userInput: \"" + answer + "\"\n" +
                "Respond with JSON in the following format in less than 20 words:\n" +
                "{ isUserInputCorrectMeaning: \"true|false\", isInputCorrectMeaningButMisspelled: \"true|false\" }");
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null, -1, null);
        String json = response.getChoices().get(0).getMessage().getContent();
        System.out.println(json);
        Gson gson = new Gson();
        ChatGptEvaluation evaluation = gson.fromJson(json, ChatGptEvaluation.class);

        System.out.println("kanji " + kanji);
        System.out.println("answer " + answer);
//        System.out.println("isInputCorrectAnswer " + evaluation.isUserInputCorrectMeaning());
//        System.out.println("isInputCorrectButMispelled " + evaluation.isInputCorrectMeaningButMisspelled());
        return evaluation.isDoesOutputConveyCorrectMeaning();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable("id") int id) {
        return languageDataDao.getQuestionById(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/question/random")
    public Question getQuestionById() {
        return languageDataDao.getRandomQuestion();
    }


    private QuestionEvaluation evaluateQuestionResponse(Question question, String answer) {

        ChatGptService chatGptService = new ChatGptService();
        BotRequest botRequest = new BotRequest();
        botRequest.setMessage("I am a "+question.getUserRole()+" and you are a "+question.getSpeakerRole()+ question.getContextLong() + " in Japan.\n" +
                "YourQuestion: "+ question.getText() +"\n" +
                "ExampleResponse: "+ question.getExampleResponse() +"\n" +
                "MyResponse: "+answer+"\n" +
                "Was my response was correct? If myResponse is in english, it is wrong. If not, explain what the question is asking and give a proper example response. Respond in JSON using three fields: \"correct\" (true/false), \"myResponseWasInJapanese\" (true/false) \"politeExplanation\" (only if correct is false)." +
                "Respond in less than 23 words.");
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null, 300, "}");
        String json = response.getChoices().get(0).getMessage().getContent() + "\n}";
        Gson gson = new Gson();
        QuestionEvaluation evaluation = gson.fromJson(json, QuestionEvaluation.class);

        System.out.println(botRequest.getMessage());
        System.out.println("correct = " + evaluation.isCorrect());
        System.out.println("inJapanese? = " + evaluation.isMyResponseWasInJapanese());
        System.out.println("comment " + evaluation.getPoliteExplanation());

        return evaluation;
    }
}
//
//    ChatGptService chatGptService = new ChatGptService();
//    BotRequest botRequest = new BotRequest();
//        botRequest.setMessage("I am a Customer and you are a Host/Hostess are at a restaurant in Japan.\n" +
//                "YourQuestion: 何名様ですか？\n" +
//                "ExampleResponse: 三にんです\n" +
//                "MyResponse: Three people\n" +
//                "Was my response was correct? If myResponse is in english, it is wrong. If not, what was the difference? Respond in JSON using two fields: \"correct\" (true/false), \"myResponseWasInJapanese\" (true/false) \"politeExplanation\" (only if correct is false).");
//                ChatGptResponse response = chatGptService.askQuestion(botRequest, null, 300, "}");
//                String json = response.getChoices().get(0).getMessage().getContent() + "\n}";
//
//                System.out.println(json);


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
