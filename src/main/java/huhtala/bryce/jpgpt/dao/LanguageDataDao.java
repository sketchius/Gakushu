package huhtala.bryce.jpgpt.dao;

import huhtala.bryce.jpgpt.model.Kanji;
import huhtala.bryce.jpgpt.model.Set;

public interface LanguageDataDao {

    public Kanji getRandomKanji();
    public Kanji getKanjiById(int id);
}
