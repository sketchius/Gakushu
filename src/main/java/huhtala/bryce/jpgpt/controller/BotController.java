package huhtala.bryce.jpgpt.controller;

import com.google.protobuf.ByteString;
import huhtala.bryce.jpgpt.model.BotRequest;
import huhtala.bryce.jpgpt.model.ChatGptResponse;
import huhtala.bryce.jpgpt.service.*;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/api/")
public class BotController {

    @Autowired
    private final ChatGptService chatGptService;
    @Autowired
    private final ConversationService conversationService;
    @Autowired
    private final TextToSpeechService textToSpeechService;
    @Autowired
    private final SpeechToTextService speechToTextService;

    public BotController(ChatGptService chatGptService, ConversationService conversationService,
                         TextToSpeechService textToSpeechService, SpeechToTextService speechToTextService) {
        this.chatGptService = chatGptService;
        this.conversationService = conversationService;
        this.textToSpeechService = textToSpeechService;
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/send")
    public ChatGptResponse sendMessage(@RequestBody BotRequest botRequest) {
        try {
            String speechInput = "";
            SpeechToTextService speechToTextService = new SpeechToTextService();
            try {
                speechInput = speechToTextService.generateTranscript(8000,"japanese");
                System.out.println("Speech Input: " + speechInput);
            } catch (Exception e) {
                e.printStackTrace();
            }


            RecorderService recorderService = new RecorderService();
            recorderService.record(5000);
            long delayEnd = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < delayEnd) {}

//            speechInput = speechToTextService.getTranscriptFromAudioFile();
//            System.out.println("Recieved transcript: " + speechInput);
//
            BotRequest speechRequest = new BotRequest(speechInput);
            conversationService.addMessage("user", speechInput);

            System.out.println("Sending ChatGPT Request...");
            ChatGptResponse response = chatGptService.askQuestion(speechRequest, conversationService);
            System.out.println("Request Recieved.");
            String responseText = response.getChoices().get(0).getMessage().getContent();
            conversationService.addMessage("assistant", responseText);
            System.out.println("");
            ByteString byteString = null;
            try {
                System.out.println("Sending TextToSpeech Request...");
                byteString = textToSpeechService.generate(responseText);
                System.out.println("Request Recieved.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("");

            System.out.println("Playing MP3...");
            InputStream targetStream = new ByteArrayInputStream(byteString.toByteArray());
            Player playMP3;
            try {
                playMP3 = new Player(targetStream);
                playMP3.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
            System.out.println("Done.");


            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/speech.mp3", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) {
        try {
            // get your file as InputStream
            File file = new File("output.mp3");
            InputStream inputStream = new DataInputStream(new FileInputStream(file));
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

}
