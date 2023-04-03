package huhtala.bryce.jpgpt.model;

public class Kanji {
    private int id;
    private String kanji;
    private int jlptLevel;
    private String onYomiReadings;
    private String kunYomiReadings;
    private String meanings;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public int getJlptLevel() {
        return jlptLevel;
    }

    public void setJlptLevel(int jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public String getOnYomiReadings() {
        return onYomiReadings;
    }

    public void setOnYomiReadings(String onYomiReadings) {
        this.onYomiReadings = onYomiReadings;
    }

    public String getKunYomiReadings() {
        return kunYomiReadings;
    }

    public void setKunYomiReadings(String kunYomiReadings) {
        this.kunYomiReadings = kunYomiReadings;
    }

    public String getMeanings() {
        return meanings;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }
}
