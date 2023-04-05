package huhtala.bryce.jpgpt;


import com.google.protobuf.ByteString;
import huhtala.bryce.jpgpt.model.BotRequest;
import huhtala.bryce.jpgpt.model.ChatGptResponse;
import huhtala.bryce.jpgpt.service.ChatGptService;
import huhtala.bryce.jpgpt.service.ConversationService;
import huhtala.bryce.jpgpt.service.SpeechToTextService;

import java.util.Scanner;

public class TestClient {



    public static void main(String[] args) {

//        while (true) {
//            ChatGptService chatGptService = new ChatGptService();
//            ConversationService conversationService = new ConversationService();
//
//            String input = getInput();
//
//            BotRequest speechRequest = new BotRequest(input);
//            conversationService.addMessage("user", input);
//
//            ChatGptResponse response = chatGptService.askQuestion(speechRequest, conversationService);
//            String responseText = response.getChoices().get(0).getMessage().getContent();
//            System.out.println("Bot:  " + responseText);
//            conversationService.addMessage("assistant", responseText);
//        }
        ChatGptService chatGptService = new ChatGptService();
        BotRequest botRequest = new BotRequest();
        botRequest.setMessage("I am a Customer and you are a Host/Hostess are at a restaurant in Japan.\n" +
                "YourQuestion: 何名様ですか？\n" +
                "ExampleResponse: 三にんです\n" +
                "MyResponse: Three people\n" +
                "Was my response was correct? If myResponse is in english, it is wrong. If not, what was the difference? Respond in JSON using two fields: \"correct\" (true/false), \"myResponseWasInJapanese\" (true/false) \"politeExplanation\" (only if correct is false).");
        ChatGptResponse response = chatGptService.askQuestion(botRequest, null, 300, "}");
        String json = response.getChoices().get(0).getMessage().getContent() + "\n}";

        System.out.println(json);


//        BotRequest botRequest2 = new BotRequest();
//        botRequest2.setMessage("Sentence: 好きは猫あります。\n" +
//                "You're a japanese language instructor. Grade this sentence on grammar and whether it's the best way to express this sentence. Respond in JSON using these fields: " +
//                "\"grammarCorrect\" (true/false),　" +
//                "\"bestWayToSayIt\" (true/false), " +
//                "\"notes\"");
//        ChatGptResponse response2 = chatGptService.askQuestion(botRequest2, null, 300, "}");
//        String json2 = response2.getChoices().get(0).getMessage().getContent() + "\n}";
//
//        System.out.println(json2);

//        ChatGptService chatGptService;
//        ConversationService conversationService;
//        TextToSpeechService textToSpeechService;
//
//        chatGptService = new ChatGptService();
//        conversationService = new ConversationService();
//        textToSpeechService = new TextToSpeechService();
//
//        BotRequest botRequest = new BotRequest();
//        botRequest.setMessage("Do they put mayonnaise on ice cream in Japan?");
//        ChatGptResponse response = chatGptService.askQuestion(botRequest, conversationService);
//
//        String responseText = response.getChoices().get(0).getMessage().getContent();
//
//        ByteString byteString = null;
//        try {
//            byteString = textToSpeechService.generate(responseText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        InputStream targetStream = new ByteArrayInputStream(byteString.toByteArray());
//        Player playMP3;
//        try {
//            playMP3 = new Player(targetStream);
//            playMP3.play();
//        } catch (JavaLayerException e) {
//            e.printStackTrace();
//        }

    }

    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("You:  ");
        return scanner.nextLine();
    }
}
