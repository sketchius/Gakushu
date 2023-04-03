package huhtala.bryce.jpgpt.model;

public class WhisperResponse {
    String text;

    public WhisperResponse(String text) {
        this.text = text;
    }

    public WhisperResponse() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
