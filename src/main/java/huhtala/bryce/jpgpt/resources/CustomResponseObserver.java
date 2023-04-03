package huhtala.bryce.jpgpt.resources;

import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;

import java.util.ArrayList;

public class CustomResponseObserver implements ResponseObserver<StreamingRecognizeResponse> {

    ArrayList<StreamingRecognizeResponse> responses = new ArrayList<>();

    private String transcriptionResult = "";

    @Override
    public void onStart(StreamController streamController) {

    }

    @Override
    public void onResponse(StreamingRecognizeResponse response) {
        responses.add(response);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable);
    }

    @Override
    public void onComplete() {
        if (responses.size() > 0) {
            for (StreamingRecognizeResponse response : responses) {
                StreamingRecognitionResult result = response.getResultsList().get(0);
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcriptionResult = alternative.getTranscript();
            }
        } else transcriptionResult = "No input.";
    }

    public String getTranscriptionResult() {
        return transcriptionResult;
    }
}
