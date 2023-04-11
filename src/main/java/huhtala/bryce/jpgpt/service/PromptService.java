package huhtala.bryce.jpgpt.service;

import com.google.gson.Gson;
import huhtala.bryce.jpgpt.model.BotRequest;
import huhtala.bryce.jpgpt.model.ChatGptEvaluation;
import huhtala.bryce.jpgpt.model.ChatGptResponse;

public class PromptService {

    public static boolean checkMeaning(ChatGptService chatGptService, String japanese, String input ) {

        String prompt =
            "Send me only JSON as a string. Do note introduce or respond or provide an explanation. Here is the JSON template to follow." +
            "{" +
            "    \"doesOutputConveyCorrectMeaning\" : (true/false)" +
            "}" +

            "Input: " + japanese +
            "Output: \""+ input + "\"" +
            "Is the translation accurate?";


        BotRequest botRequest = new BotRequest();
        botRequest.setMessage(prompt);
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null, -1, "}");
        String json = response.getChoices().get(0).getMessage().getContent() + "}";
        System.out.println(json);
        Gson gson = new Gson();
        ChatGptEvaluation evaluation = gson.fromJson(json, ChatGptEvaluation.class);

        return evaluation.isDoesOutputConveyCorrectMeaning();

    }

    public static boolean checkJapaneseTranslation(ChatGptService chatGptService, String english, String input ) {

        String prompt =
                "Send me only JSON as a string. Do note introduce or respond or provide an explanation. Here is the JSON template to follow." +
                        "{" +
                        "    \"doesOutputConveyCorrectMeaning\" : (true/false)" +
                        "}" +

                        "Input: " + english +
                        "Output: \""+ input + "\"" +
                        "Is the translation accurate?";


        BotRequest botRequest = new BotRequest();
        botRequest.setMessage(prompt);
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null, -1, "}");
        String json = response.getChoices().get(0).getMessage().getContent() + "}";
        System.out.println(json);
        Gson gson = new Gson();
        ChatGptEvaluation evaluation = gson.fromJson(json, ChatGptEvaluation.class);

        return evaluation.isDoesOutputConveyCorrectMeaning();

    }
}
