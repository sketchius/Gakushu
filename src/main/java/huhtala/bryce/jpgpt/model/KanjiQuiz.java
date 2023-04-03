package huhtala.bryce.jpgpt.model;

public class KanjiQuiz {

    private int id;
    private String kanji;

    public KanjiQuiz(int id, String primaryContent, String alternameContent, String teriaryContent, String itemTypeName) {
        this.id = id;
        this.kanji = primaryContent;
    }

    public KanjiQuiz(Kanji kanji) {
        this.id = kanji.getId();
        this.kanji = kanji.getKanji();
    }

    public KanjiQuiz() {
    }

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
}
