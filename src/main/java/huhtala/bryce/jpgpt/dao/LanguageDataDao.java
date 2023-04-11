package huhtala.bryce.jpgpt.dao;

import huhtala.bryce.jpgpt.model.Item;
import huhtala.bryce.jpgpt.model.Kanji;
import huhtala.bryce.jpgpt.model.Question;

public interface LanguageDataDao {

    public Question getQuestionById(int id);
    public Question getRandomQuestion();
    public Item getChallengeItem();
    public Item getItemById(int id);
}
