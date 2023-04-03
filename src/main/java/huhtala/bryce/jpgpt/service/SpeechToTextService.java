package huhtala.bryce.jpgpt.service;

import com.google.api.gax.rpc.ClientStream;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import huhtala.bryce.jpgpt.model.*;
import huhtala.bryce.jpgpt.resources.ChatGptConfig;
import huhtala.bryce.jpgpt.resources.CustomResponseObserver;
import huhtala.bryce.jpgpt.resources.WhisperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;

@Component
public class SpeechToTextService {

    @Value("${openAIKey}")
    private String API_KEY;
    private static final RestTemplate restTemplate = new RestTemplate();


    public WhisperResponse getResponse(HttpEntity<MultiValueMap<String, Object>> whisperRequestHttpEntity) {
        ResponseEntity<WhisperResponse> responseEntity = restTemplate.postForEntity(WhisperConfig.URL,
                whisperRequestHttpEntity,
                WhisperResponse.class);
        return responseEntity.getBody();
    }

    public String getTranscriptFromAudioFile() {
        SpeechToTextService s = new SpeechToTextService();
        Path filePath = null;
        try {
            URL audioUrl = SpeechToTextService.class.getResource("output.wav");

            if (audioUrl != null) {
                filePath = Path.of(audioUrl.toURI());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return getResponse(createRequestEntity(new File(filePath.toString()))).getText();
    }

    public String generateTranscript(int durationInMillis, String language) throws Exception {

        String languageCode = "";

        switch (language.toLowerCase(Locale.ROOT)) {
            default:
            case "english":
                languageCode = "en-US";
                break;
            case "japanese":
                languageCode = "ja-JP";
                break;
        }

        CustomResponseObserver responseObserver = null;
        try (SpeechClient client = SpeechClient.create()) {


            responseObserver =
                    new CustomResponseObserver();

            ClientStream<StreamingRecognizeRequest> clientStream =
                    client.streamingRecognizeCallable().splitCall(responseObserver);

            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setLanguageCode(languageCode)
                            .setModel("latest_long")
                            .setSampleRateHertz(16000)
                            .build();
            StreamingRecognitionConfig streamingRecognitionConfig =
                    StreamingRecognitionConfig.newBuilder().setConfig(recognitionConfig).build();

            StreamingRecognizeRequest request =
                    StreamingRecognizeRequest.newBuilder()
                            .setStreamingConfig(streamingRecognitionConfig)
                            .build(); // The first request in a streaming call has to be a config

            clientStream.send(request);
            // SampleRate:16000Hz, SampleSizeInBits: 16, Number of channels: 1, Signed: true,
            // bigEndian: false
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat); // Set the system information to read from the microphone audio stream

            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("Start speaking");
            long startTime = System.currentTimeMillis();
            // Audio Input Stream
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[6400];
                audio.read(data);
                if (estimatedTime > durationInMillis) { // 6 seconds
                    System.out.println("Stop speaking.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    break;
                }
                request =
                        StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build();
                clientStream.send(request);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        responseObserver.onComplete();
        return responseObserver.getTranscriptionResult();
    }


    private HttpEntity<MultiValueMap<String, Object>> createRequestEntity(File file) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", file);
        body.add("model", WhisperConfig.MODEL);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + API_KEY);
        return new HttpEntity<>(body, headers);
    }
}
