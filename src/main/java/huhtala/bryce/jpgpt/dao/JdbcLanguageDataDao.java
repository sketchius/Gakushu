package huhtala.bryce.jpgpt.dao;

import huhtala.bryce.jpgpt.model.Kanji;
import huhtala.bryce.jpgpt.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import huhtala.bryce.jpgpt.model.Set;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public Kanji getKanjiById(int id) {
        String sql = "SELECT item_id, kanji, jlpt_level, on_yomi_readings, kun_yomi_readings, meanings FROM kanji\n" +
                "WHERE item_id = ?";
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
    public Kanji getRandomKanji() {
        List<Kanji> setKanjis = new ArrayList<>();
        String sql = "SELECT item_id, kanji, jlpt_level, on_yomi_readings, kun_yomi_readings, meanings FROM kanji\n";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            setKanjis.add(mapItemDTO(rowSet));
        }
        if (setKanjis.size() > 1) {
            Random random = new Random();
            return setKanjis.get(random.nextInt(setKanjis.size()));
        }
        return null;
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

    public Kanji mapItemDTO(SqlRowSet sqlRowSet) {
        Kanji kanji = new Kanji();
        kanji.setId(sqlRowSet.getInt("item_id"));
        kanji.setKanji(sqlRowSet.getString("kanji"));
        kanji.setJlptLevel(sqlRowSet.getInt("jlpt_level"));
        kanji.setOnYomiReadings(sqlRowSet.getString("on_yomi_readings"));
        kanji.setKunYomiReadings(sqlRowSet.getString("kun_yomi_readings"));
        kanji.setMeanings(sqlRowSet.getString("meanings"));
        return kanji;
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
