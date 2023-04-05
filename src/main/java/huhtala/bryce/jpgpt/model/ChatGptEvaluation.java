package huhtala.bryce.jpgpt.model;

public class ChatGptEvaluation {
    boolean isUserInputCorrectMeaning;
    boolean isInputCorrectMeaningButMisspelled;

    public ChatGptEvaluation(boolean isUserInputCorrectMeaning, boolean isInputCorrectMeaningButMisspelled) {
        this.isUserInputCorrectMeaning = isUserInputCorrectMeaning;
        this.isInputCorrectMeaningButMisspelled = isInputCorrectMeaningButMisspelled;
    }

    public ChatGptEvaluation() {
    }

    public boolean isUserInputCorrectMeaning() {
        return isUserInputCorrectMeaning;
    }

    public void setUserInputCorrectMeaning(boolean userInputCorrectMeaning) {
        isUserInputCorrectMeaning = userInputCorrectMeaning;
    }

    public boolean isInputCorrectMeaningButMisspelled() {
        return isInputCorrectMeaningButMisspelled;
    }

    public void setInputCorrectMeaningButMisspelled(boolean inputCorrectMeaningButMisspelled) {
        isInputCorrectMeaningButMisspelled = inputCorrectMeaningButMisspelled;
    }
}
