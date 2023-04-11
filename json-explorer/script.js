let myJson;

fetch('./kanji.json')
    .then((response) => response.json())
    .then((json) => {
        myJson = json
        myArray = [];

        console.log(typeof myJson)
        for (const [key, value] of Object.entries(myJson)) {
            value.kanji = key;
            myArray.push(value);
        }

        myArray = myArray.filter(entry => entry.jlpt_new == 5);

        console.log(myArray);

        let scriptString = "INSERT INTO kanji (kanji, jlpt_level, on_yomi_readings, kun_yomi_readings, meanings)\n";


        myArray.forEach(entry => {

            const on_readings = entry.readings_on.reduce((full, current, index) => {
                if (index > 0)
                    return full + "|" + current;
                else
                    return full + current;
            }, "");
            let kun_readings = "";
            if (typeof entry.readings_kun == "object") {
                if (entry.readings_kun.length > 0){
                kun_readings = entry.readings_kun.reduce((full, current, index) => {
                    if (index > 0)
                        return full + "|" + current;
                    else
                        return full + current;
                }, "")}
            };
            const meanings = entry.meanings.reduce((full, current, index) => {
                if (typeof current == "string" && !current.includes("Radical")){
                if (index > 0)
                    return full + "|" + current;
                else
                    return full + current;
                } else return full;
            });
            // scriptString = scriptString + `('${entry.kanji}', 'kanji', '${entry.jlpt_new}', '${on_readings}', '${kun_readings}', '${meanings}'),\n`;
            // scriptString = scriptString + `kanji\t${entry.kanji}\t${entry.jlpt_new}\t${on_readings}\t${kun_readings}\t${meanings}\n`;
            // scriptString = scriptString + `${kun_readings}\t;${on_readings}\t\n`;
        })

        console.log(scriptString);



    });


