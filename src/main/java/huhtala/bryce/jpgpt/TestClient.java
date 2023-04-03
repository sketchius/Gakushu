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

        while (true) {
            ChatGptService chatGptService = new ChatGptService();
            ConversationService conversationService = new ConversationService();

            String input = getInput();

            BotRequest speechRequest = new BotRequest(input);
            conversationService.addMessage("user", input);

            ChatGptResponse response = chatGptService.askQuestion(speechRequest, conversationService);
            String responseText = response.getChoices().get(0).getMessage().getContent();
            System.out.println("Bot:  " + responseText);
            conversationService.addMessage("assistant", responseText);
        }


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
