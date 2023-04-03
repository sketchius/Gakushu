package huhtala.bryce.jpgpt.dao;

import huhtala.bryce.jpgpt.model.Kanji;
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
        String sql = "SELECT id, kanji, jlpt_level, on_yomi_readings, kun_yomi_readings, meanings FROM kanji\n" +
                "WHERE id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapItemDTO(rowSet);
        } else {
            return null;
        }
    }

    @Override
    public Kanji getRandomKanji() {
        List<Kanji> setKanjis = new ArrayList<>();
        String sql = "SELECT id, kanji, jlpt_level, on_yomi_readings, kun_yomi_readings, meanings FROM kanji\n";
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

    public Set mapSetDTO(SqlRowSet sqlRowSet) {
        Set set = new Set();
        set.setId(sqlRowSet.getInt("id"));
        set.setName(sqlRowSet.getString("name"));
        set.setItemTypeId(sqlRowSet.getInt("item_type_id"));
        return set;
    }

    public Kanji mapItemDTO(SqlRowSet sqlRowSet) {
        Kanji kanji = new Kanji();
        kanji.setId(sqlRowSet.getInt("id"));
        kanji.setKanji(sqlRowSet.getString("kanji"));
        kanji.setJlptLevel(sqlRowSet.getInt("jlpt_level"));
        kanji.setOnYomiReadings(sqlRowSet.getString("on_yomi_readings"));
        kanji.setKunYomiReadings(sqlRowSet.getString("kun_yomi_readings"));
        kanji.setMeanings(sqlRowSet.getString("meanings"));
        return kanji;
    }
}
