package huhtala.bryce.jpgpt.dao;

import huhtala.bryce.jpgpt.model.Item;
import huhtala.bryce.jpgpt.model.ItemComparator;
import huhtala.bryce.jpgpt.model.Kanji;
import huhtala.bryce.jpgpt.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Repository
public class JdbcLanguageDataDao implements LanguageDataDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public JdbcLanguageDataDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public Item getItemById(int id) {
        String sql = "SELECT item.item_id, cat.item_category_name, item.context, item.jlpt_level, item.question_candidate, item.text, item.hiragana, item.meaning, COALESCE(progress.correct, 0) AS correct, COALESCE(progress.incorrect, 0) AS incorrect\n" +
                "FROM item\n" +
                "LEFT JOIN user_progress progress ON progress.item_id = item.item_id AND progress.user_id = 1\n" +
                "JOIN item_category cat ON item.item_category_id = cat.item_category_id\n" +
                "WHERE item.item_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapItemDTO(rowSet);
        } else {
            return null;
        }
    }

    @Override
    public Question getQuestionById(int id) {
        List<Kanji> setKanjis = new ArrayList<>();
        String sql = "SELECT question.item_id, context.context, context.context_long, " +
                "user_role.role_name AS user_role, speaker_role.role_name AS speaker_role, text, " +
                "text_hiragana, meaning, example_response, example_response_hiragana, " +
                "example_response_meaning FROM question\n" +
                "JOIN context ON question.context_id = context.context_id\n" +
                "JOIN conversation_role AS user_role ON question.user_role_id = user_role.role_id\n" +
                "JOIN conversation_role AS speaker_role ON question.speaker_role_id = speaker_role.role_id\n" +
                "WHERE item_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapQuestionDTO(rowSet);
        } else {
            return null;
        }
    }

    @Override
    public Item getChallengeItem() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT item.item_id, cat.item_category_name, item.context, item.jlpt_level, item.question_candidate, item.text, item.hiragana, item.meaning, COALESCE(progress.correct, 0) AS correct, COALESCE(progress.incorrect, 0) AS incorrect\n" +
                "FROM item\n" +
                "LEFT JOIN user_progress progress ON progress.item_id = item.item_id AND progress.user_id = 1\n" +
                "JOIN item_category cat ON item.item_category_id = cat.item_category_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            items.add(mapItemDTO(rowSet));
        }
        Collections.sort(items, new ItemComparator());

        return items.get(0);
    }

    @Override
    public Question getRandomQuestion() {
        List<Question> allQuestions = new ArrayList<>();
        String sql = "SELECT question.item_id, context.context, context.context_long, " +
                "user_role.role_name AS user_role, speaker_role.role_name AS speaker_role, text, " +
                "text_hiragana, meaning, example_response, example_response_hiragana, " +
                "example_response_meaning FROM question\n" +
                "JOIN context ON question.context_id = context.context_id\n" +
                "JOIN conversation_role AS user_role ON question.user_role_id = user_role.role_id\n" +
                "JOIN conversation_role AS speaker_role ON question.speaker_role_id = speaker_role.role_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            allQuestions.add(mapQuestionDTO(rowSet));
        }
        if (allQuestions.size() > 1) {
            Random random = new Random();
            return allQuestions.get(random.nextInt(allQuestions.size()));
        }
        return null;
    }





//    public Set mapSetDTO(SqlRowSet sqlRowSet) {
//        Set set = new Set();
//        set.setId(sqlRowSet.getInt("id"));
//        set.setName(sqlRowSet.getString("name"));
//        set.setItemTypeId(sqlRowSet.getInt("item_type_id"));
//        return set;
//    }

    public Item mapItemDTO(SqlRowSet sqlRowSet) {
        Item item = new Item();
        item.setItemId(sqlRowSet.getInt("item_id"));
        item.setItemCategoryName(sqlRowSet.getString("item_category_name"));
        item.setContext(sqlRowSet.getString("context"));
        item.setJlptLevel(sqlRowSet.getInt("jlpt_level"));
        item.setQuestionCandidate(sqlRowSet.getBoolean("question_candidate"));
        item.setText(sqlRowSet.getString("text"));
        item.setHiragana(sqlRowSet.getString("hiragana"));
        item.setMeaning(sqlRowSet.getString("meaning"));
        item.setCorrect(sqlRowSet.getInt("correct"));
        item.setIncorrect(sqlRowSet.getInt("incorrect"));
        return item;
    }

    public Question mapQuestionDTO(SqlRowSet sqlRowSet) {
        Question question = new Question();
        question.setId(sqlRowSet.getInt("item_id"));
        question.setContext(sqlRowSet.getString("context"));
        question.setContextLong(sqlRowSet.getString("context_long"));
        question.setUserRole(sqlRowSet.getString("user_role"));
        question.setSpeakerRole(sqlRowSet.getString("speaker_role"));
        question.setText(sqlRowSet.getString("text"));
        question.setTextHiragana(sqlRowSet.getString("text_hiragana"));
        question.setMeaning(sqlRowSet.getString("meaning"));
        question.setExampleResponse(sqlRowSet.getString("example_response"));
        question.setExampleResponseHiragana(sqlRowSet.getString("example_response_hiragana"));
        question.setExampleResponseMeaning(sqlRowSet.getString("example_response_meaning"));
        return question;
    }
}
