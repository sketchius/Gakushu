<template>
  <main
    translate="no"
    class="container"
    :class="[
      displayMode == 'prompt' ? 'prompt' : correct ? 'correct' : 'incorrect',
    ]"
  >
    <section class="grid">
      <h2 id="output">
        <div lang="ja" class="japanese">{{ this.outputJapanese }}</div>
        <div class="english">{{ this.outputEnglish }}</div>
      </h2>
      <div id="content-container">
        <h3 lang="ja" id="content" class="japanese">
          {{ this.challenge.kanji }}
        </h3>
      </div>

      <div>
        <input
          ref="inputField"
          v-show="displayMode == 'prompt'"
          v-model="input"
          type="text"
        />
        <div
          v-show="displayMode != 'prompt'"
          :class="[correct ? 'correct' : 'incorrect']"
          id="entered-input"
        >
          {{ this.input }}
        </div>

        <div class="buttons">
          <button>Submit</button>
        </div>
      </div>
    </section>
  </main>
</template>

<script>
import apiService from "../services/ApiService.ts";

export default {
  name: "study-view",
  data() {
    return {
      displayMode: "prompt",
      input: "",
      outputJapanese: "",
      outputEnglish: "",
      correct: false,
      challenge: {
        id: -1,
        kanji: "",
      },
    };
  },
  methods: {
    processAnswer() {
      if (this.displayMode == "prompt") {
        apiService
          .checkAnswer(this.challenge.id, this.input)
          .then((response) => {
            const evaluation = response.data.evaluation;
            switch (evaluation) {
              case "Correct":
                this.outputJapanese = "せいかい";
                this.outputEnglish = "Correct!";
                this.correct = true;
                this.displayMode = "result";
                break;
              case "Incorrect":
                this.outputJapanese = "ちがいです";
                this.outputEnglish = "That's not right.";
                this.correct = false;
                this.displayMode = "result";
                break;
              case "LanguageError":
                this.outputJapanese = "ちがいです";
                this.outputEnglish = "Your answer must be in English!";
                this.correct = false;
                this.displayMode = "result";
                break;
            }
          });
      } else {
        this.setPrompt();
        this.focusInputField();
        this.getNewChallenge();
      }
    },
    focusInputField() {
      this.$nextTick(() => {
        const inputFieldRef = this.$refs.inputField;
        inputFieldRef.focus();
      });
    },
    getNewChallenge() {
      apiService.requestChallenge().then((response) => {
        console.log("Got response: ");
        console.log(response.data);
        this.challenge = response.data;
      });
    },
    setPrompt() {
      this.displayMode = "prompt";
      this.outputJapanese = "このかんじのいみはなんですか？";
      this.outputEnglish = "What is does this Kanji mean?";
      this.input = "";
      this.correct = false;
    },
  },
  mounted() {
    // eslint-disable-next-line
    let self = this;
    self.focusInputField();
    self.getNewChallenge();
    self.setPrompt();

    window.addEventListener("keyup", function (event) {
      if (event.key == "Enter" && self.input != "") self.processAnswer();
    });
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Serif+JP:wght@500&family=Open+Sans:wght@300;400&display=swap");
* {
  font-family: "Open Sans", sans-serif;
  font-weight: 300;
}
main {
  --primary100: rgb(241, 251, 255);
  --primary200: rgb(210, 233, 255);
  --primary300: rgb(129, 145, 218);
  --primary500: rgb(75, 68, 177);
  --primary700: rgb(53, 39, 116);
  --primary900: rgb(29, 17, 56);
  --success100: rgb(241, 253, 255);
  --success200: rgb(210, 255, 251);
  --success300: rgb(129, 218, 211);
  --success500: rgb(68, 177, 153);
  --success700: rgb(39, 116, 97);
  --success900: rgb(17, 56, 40);
  --mistake100: rgb(255, 241, 244);
  --mistake200: rgb(255, 210, 227);
  --mistake300: rgb(218, 129, 171);
  --mistake500: rgb(177, 68, 141);
  --mistake700: rgb(116, 39, 99);
  --mistake900: rgb(56, 17, 56);
}
main.prompt {
  --current100: var(--primary100);
  --current200: var(--primary200);
  --current300: var(--primary300);
  --current500: var(--primary500);
  --current700: var(--primary700);
  --current900: var(--primary900);
}
main.correct {
  --current100: var(--success100);
  --current200: var(--success200);
  --current300: var(--success300);
  --current500: var(--success500);
  --current700: var(--success700);
  --current900: var(--success900);
}
main.incorrect {
  --current100: var(--mistake100);
  --current200: var(--mistake200);
  --current300: var(--mistake300);
  --current500: var(--mistake500);
  --current700: var(--mistake700);
  --current900: var(--mistake900);
}
.container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background: linear-gradient(to top, var(--current300), var(--current100));
}

/* .container.correct {
  background: linear-gradient(to top, rgb(210, 255, 234), rgb(241, 255, 246));
}
.container.incorrect {
  background: linear-gradient(to top, rgb(255, 210, 220), rgb(255, 241, 248));
} */
.grid {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 20vh 40vh fit-content fit-content;
  align-items: center;
  justify-items: center;
}
#content {
  margin: 0;
  margin-bottom: 0rem;
  font-size: 10rem;
  color: var(--current500);
  position: relative;
  bottom: 0.5rem;
}
#content-container {
  background: radial-gradient(
    circle at center,
    var(--current100),
    rgba(255, 255, 255, 0)
  );
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  border: 5px var(--current300) double;
  border-radius: 50rem;
  width: 15rem;
  height: 15rem;
}
input[type="text"] {
  width: min(40vw, 400px);
  color: var(--current700);
  border: none;
  margin: 2rem 0;
  margin-bottom: 1rem;
  border-radius: 2rem;
  font-weight: 400;
  padding: 10px 15px;
  font-size: 2rem;
  text-align: center;
  box-shadow: 0 0 1rem var(--current300);
}
input[type="text"]:focus {
  outline: none !important;
  /* border: 1px solid rgb(82, 82, 161); */
}
#entered-input {
  width: min(40vw, 400px);
  color: var(--current700);
  background-color: #eff7ff;
  border: none;
  margin: 2rem 0;
  margin-bottom: 1rem;
  font-weight: 400;
  padding: 10px 15px;
  border-radius: 2rem;
  font-size: 2rem;
  text-align: center;
  box-shadow: 0 0 1rem var(--current300);
}
#entered-input.correct {
  color: rgb(2, 173, 102);
  background-color: rgb(240, 255, 250);
}
#entered-input.incorrect {
  color: rgb(173, 2, 45);
  /* box-shadow: 0 0 1rem rgb(228, 152, 171); */
}
#output .english,
#content .english {
  color: rgb(36, 2, 173);
  font-weight: 400;
  font-size: 3rem;
}
#output .japanese,
#content .japanese {
  color: rgb(129, 103, 214);
  font-size: 2.5rem;
}
.correct #output .english,
.correct #content,
.correct input[type="text"] {
  color: rgb(2, 173, 102);
}
.correct #output .japanese {
  color: rgb(103, 214, 171);
}
.incorrect #output .english,
.incorrect #content,
.incorrect input[type="text"] {
  color: rgb(173, 2, 45);
}
.incorrect #output .japanese {
  color: rgb(214, 103, 131);
}
.japanese {
  font-family: "Noto Serif JP", serif;
  line-height: 100%;
}
.buttons {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}
.buttons button {
  background-color: var(--current500);
  color: white;
  font-size: 1rem;
  border: none;
  border-radius: 3rem;
  padding: 0.5rem 2rem;
}
</style>
