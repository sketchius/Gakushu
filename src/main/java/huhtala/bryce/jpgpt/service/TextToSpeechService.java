package huhtala.bryce.jpgpt.service;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Component;

@Component
public class TextToSpeechService {

    public ByteString generate(String input) throws Exception {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput synthesisInput = SynthesisInput.newBuilder().setText(input).build();

            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ja-JP")
                            .setSsmlGender(SsmlVoiceGender.MALE)
                            .build();

            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();


            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(synthesisInput, voice, audioConfig);

            return response.getAudioContent();
        }
    }
}