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
