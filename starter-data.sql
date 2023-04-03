INSERT INTO item_type (name) VALUES ('kanji');

INSERT INTO set (name, item_type_id)
VALUES ('Set 1', 1);

INSERT INTO item (set_id, character, alternate_form, readings, meanings)
VALUES (1, '人', 'ひと', 'じん、にん', 'person');

INSERT INTO item (set_id, character, alternate_form, readings, meanings)
VALUES (1, '木', 'き', 'もく', 'tree');

INSERT INTO item (set_id, character, alternate_form, readings, meanings)
VALUES (1, '水', 'みず', 'すい', 'water');

INSERT INTO item (set_id, character, alternate_form, readings, meanings)
VALUES (1, '火', 'ひ', 'か', 'fire');

INSERT INTO item (set_id, character, alternate_form, readings, meanings)
VALUES (1, '金', 'かね', 'きん', 'gold');