package huhtala.bryce.jpgpt.model;

public class ChatGptEvaluation {
    private boolean doesOutputConveyCorrectMeaning;

    public ChatGptEvaluation(boolean doesOutputConveyCorrectMeaning) {
        this.doesOutputConveyCorrectMeaning = doesOutputConveyCorrectMeaning;
    }

    public ChatGptEvaluation() {
    }

    public boolean isDoesOutputConveyCorrectMeaning() {
        return doesOutputConveyCorrectMeaning;
    }

    public void setDoesOutputConveyCorrectMeaning(boolean doesOutputConveyCorrectMeaning) {
        this.doesOutputConveyCorrectMeaning = doesOutputConveyCorrectMeaning;
    }
}
