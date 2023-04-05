package huhtala.bryce.jpgpt.model;

public class Question {
    int id;
    String context;
    String contextLong;
    String userRole;
    String speakerRole;
    String text;
    String textHiragana;
    String meaning;
    String exampleResponse;
    String exampleResponseHiragana;
    String exampleResponseMeaning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextLong() {
        return contextLong;
    }

    public void setContextLong(String contextLong) {
        this.contextLong = contextLong;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getSpeakerRole() {
        return speakerRole;
    }

    public void setSpeakerRole(String speakerRole) {
        this.speakerRole = speakerRole;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextHiragana() {
        return textHiragana;
    }

    public void setTextHiragana(String textHiragana) {
        this.textHiragana = textHiragana;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExampleResponse() {
        return exampleResponse;
    }

    public void setExampleResponse(String exampleResponse) {
        this.exampleResponse = exampleResponse;
    }

    public String getExampleResponseHiragana() {
        return exampleResponseHiragana;
    }

    public void setExampleResponseHiragana(String exampleResponseHiragana) {
        this.exampleResponseHiragana = exampleResponseHiragana;
    }

    public String getExampleResponseMeaning() {
        return exampleResponseMeaning;
    }

    public void setExampleResponseMeaning(String exampleResponseMeaning) {
        this.exampleResponseMeaning = exampleResponseMeaning;
    }
}
