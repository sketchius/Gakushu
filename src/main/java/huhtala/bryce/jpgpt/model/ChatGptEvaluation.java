package huhtala.bryce.jpgpt.model;

public class ChatGptEvaluation {
    boolean isInputWrittenWithEnglish;
    boolean isInputCorrectAnswer;

    public ChatGptEvaluation(boolean isInputWrittenInEnglish, boolean isInputCorrectAnswer) {
        this.isInputWrittenWithEnglish = isInputWrittenInEnglish;
        this.isInputCorrectAnswer = isInputCorrectAnswer;
    }

    public ChatGptEvaluation() {
    }

    public boolean isInputWrittenWithEnglish() {
        return isInputWrittenWithEnglish;
    }

    public void setInputWrittenWithEnglish(boolean inputWrittenWithEnglish) {
        isInputWrittenWithEnglish = inputWrittenWithEnglish;
    }

    public boolean isInputCorrectAnswer() {
        return isInputCorrectAnswer;
    }

    public void setInputCorrectAnswer(boolean inputCorrectAnswer) {
        isInputCorrectAnswer = inputCorrectAnswer;
    }
}
