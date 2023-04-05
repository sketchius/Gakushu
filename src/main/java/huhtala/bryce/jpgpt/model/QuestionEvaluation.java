package huhtala.bryce.jpgpt.model;

public class QuestionEvaluation {
    boolean correct;
    boolean myResponseWasInJapanese;
    String politeExplanation;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isMyResponseWasInJapanese() {
        return myResponseWasInJapanese;
    }

    public void setMyResponseWasInJapanese(boolean myResponseWasInJapanese) {
        this.myResponseWasInJapanese = myResponseWasInJapanese;
    }

    public String getPoliteExplanation() {
        return politeExplanation;
    }

    public void setPoliteExplanation(String politeExplanation) {
        this.politeExplanation = politeExplanation;
    }
}
