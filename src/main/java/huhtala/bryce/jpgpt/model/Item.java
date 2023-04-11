package huhtala.bryce.jpgpt.model;

import java.util.Random;

public class Item {
    private int itemId;
    private String itemCategoryName;
    private String context;
    private int jlptLevel;
    private boolean questionCandidate;
    private String text;
    private String hiragana;
    private String meaning;
    private int correct;
    private int incorrect;
    private double selectionValue;

    public Item(int itemId, String itemCategoryName, String context, int jlptLevel, boolean questionCandidate, String text, String hiragana, String meaning, int correct, int incorrect) {
        this.itemId = itemId;
        this.itemCategoryName = itemCategoryName;
        this.context = context;
        this.jlptLevel = jlptLevel;
        this.questionCandidate = questionCandidate;
        this.text = text;
        this.hiragana = hiragana;
        this.meaning = meaning;
        this.correct = correct;
        this.incorrect = incorrect;
        Random random = new Random();
        selectionValue = random.nextDouble();
    }

    public Item() {
        Random random = new Random();
        selectionValue = random.nextDouble();
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isQuestionCandidate() {
        return questionCandidate;
    }

    public void setQuestionCandidate(boolean questionCandidate) {
        this.questionCandidate = questionCandidate;
    }

    public String getText() {
        if (text.equalsIgnoreCase("NULL") || text.equalsIgnoreCase(""))
            return hiragana;
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHiragana() {
        return hiragana;
    }

    public void setHiragana(String hiragana) {
        this.hiragana = hiragana;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(int jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public double getSelectionValue() {
        return selectionValue;
    }
}
