<template>
  <main
    translate="no"
    class="container"
    :class="[
      displayMode == 'prompt' ? 'prompt' : correct ? 'correct' : 'incorrect',
    ]"
  >
    <section class="grid">
      <output-component
        v-bind:output-japanese="this.outputJapanese"
        v-bind:output-english="this.outputEnglish"
      />

      <div id="content-container">
        <h3 lang="ja" id="content" class="japanese">
          {{ challenge.kanji }}
        </h3>
      </div>
      <div id="inputControls">
        <div id="inputWithButton">
          <label id="inputLabel">Answer:</label>
          <div id="inputContainer">
            <input
              autocomplete="off"
              id="textInput"
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
          </div>
          <button v-on:click="handleSubmit()" v-show="displayMode == 'result'">
            Next
          </button>
          <button v-on:click="handleSubmit()" v-show="displayMode == 'prompt'">
            Send
          </button>
        </div>

        <div class="buttons">
          <button
            v-on:click="handleHint()"
            v-show="displayMode == 'prompt' && mode != 'hint'"
          >
            <span class="hotkey">~</span> Hint
          </button>
          <button v-on:click="handleTryAgain()" v-show="mode == 'incorrect'">
            <span class="hotkey">T</span>Try Again
          </button>
          <button v-on:click="handleShowAnswer()" v-show="mode == 'incorrect'">
            <span class="hotkey">A</span>See Answer
          </button>
          <button
            v-on:click="handleAppealAnswer()"
            v-show="mode == 'showAnswer'"
          >
            <span class="hotkey">E</span>Appeal Evaluation
          </button>
        </div>
      </div>
    </section>
  </main>
</template>

<script>
import apiService from "../services/ApiService.ts";
import outputComponent from "../components/Output.vue";

export default {
  name: "study-view",
  data() {
    return {
      displayMode: "prompt",
      mode: "",
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
                this.mode = "correct";
                break;
              case "Incorrect":
                this.outputJapanese = "ちがいです";
                this.outputEnglish = "That's not right.";
                this.correct = false;
                this.displayMode = "result";
                this.mode = "incorrect";
                break;
              case "LanguageError":
                this.outputJapanese = "ちがいです";
                this.outputEnglish = "Your answer must be in English!";
                this.correct = false;
                this.displayMode = "result";
                this.mode = "incorrect";
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
      this.mode = "prompt";
      this.correct = false;
    },
    handleHint() {
      apiService.getMeaning(this.challenge.id).then((response) => {
        this.mode = "hint";
        console.log(response.data);
        const firstMeaning = response.data[0];
        const rnd = Math.random(2);
        if (Math.random(1) < 0.5) {
          this.outputEnglish =
            "What is does this Kanji mean? " +
            "(Hint: " +
            firstMeaning.length +
            " letters)";
        } else {
          this.outputEnglish =
            "What is does this Kanji mean? " +
            `(Hint: Starts with "` +
            firstMeaning.charAt(0) +
            `")`;
        }
      });
    },
    handleSubmit() {
      if (this.input != "") this.processAnswer();
    },
    handleTryAgain() {
      this.setPrompt();
      this.focusInputField();
    },
    handleShowAnswer() {
      apiService.getMeaning(this.challenge.id).then((response) => {
        this.mode = "showAnswer";
        this.outputJapanese = "";
        const answers = response.data.join(", ");
        this.outputEnglish = "The correct answer is: \n" + answers;
      });
    },
    handleAppealAnswer() {
      this.outputEnglish = "Computing...";
      apiService
        .appealAnswer(this.challenge.id, this.input)
        .then((response) => {
          const evaluation = response.data.evaluation;
          switch (evaluation) {
            case "Correct":
              this.outputJapanese = "せいかい";
              this.outputEnglish = "Your answer is accepted!";
              this.correct = true;
              this.displayMode = "result";
              this.mode = "appealCorrect";
              break;
            case "Incorrect":
              this.outputJapanese = "ちがいです";
              this.outputEnglish = "Sorry, it's not right.";
              this.correct = false;
              this.displayMode = "result";
              this.mode = "appealIncorrect";
              break;
          }
        });
    },
  },
  mounted() {
    // eslint-disable-next-line
    let self = this;
    self.focusInputField();
    self.getNewChallenge();
    self.setPrompt();

    document
      .getElementById("textInput")
      .addEventListener("keydown", (event) => {
        if (event.key == "`" || event.key == "~") {
          event.preventDefault();
        }
      });

    window.addEventListener("keyup", function (event) {
      switch (event.key.toLowerCase()) {
        case "enter":
          if (self.input != "") self.processAnswer();
          break;
        case "t":
          if (self.mode == "incorrect") self.handleTryAgain();
          break;
        case "a":
          event.preventDefault();
          if (self.mode == "incorrect") self.handleShowAnswer();
          break;
        case "e":
          if (self.mode == "showAnswer") self.handleAppealAnswer();
          break;
        case "`":
        case "~":
          if (self.input.charAt(self.input.length - 1) == "`")
            self.input = self.input.substr(0, self.input.length - 1);
          if (self.mode == "prompt") self.handleHint();
          break;
      }
    });
  },
  components: {
    outputComponent,
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
  --primary400: rgb(101, 109, 221);
  --primary500: rgb(75, 68, 177);
  --primary700: rgb(53, 39, 116);
  --primary900: rgb(29, 17, 56);
  --success100: rgb(241, 253, 255);
  --success200: rgb(176, 246, 240);
  --success300: rgb(129, 218, 211);
  --success400: rgb(81, 195, 171);
  --success500: rgb(68, 177, 153);
  --success700: rgb(39, 116, 97);
  --success900: rgb(17, 56, 40);
  --mistake100: rgb(255, 241, 244);
  --mistake200: rgb(255, 210, 227);
  --mistake300: rgb(218, 129, 171);
  --mistake400: rgb(211, 96, 167);
  --mistake500: rgb(177, 68, 141);
  --mistake700: rgb(116, 39, 99);
  --accent100: rgb(255, 254, 241);
  --accent200: rgb(254, 255, 210);
  --accent300: rgb(218, 217, 129);
  --mistake400: rgb(211, 96, 167);
  --mistake500: rgb(177, 68, 141);
  --mistake700: rgb(116, 39, 99);
}
main.prompt {
  --current100: var(--primary100);
  --current200: var(--primary200);
  --current300: var(--primary300);
  --current400: var(--primary400);
  --current500: var(--primary500);
  --current700: var(--primary700);
  --current900: var(--primary900);
}
main.correct {
  --current100: var(--success100);
  --current200: var(--success200);
  --current300: var(--success300);
  --current400: var(--success400);
  --current500: var(--success500);
  --current700: var(--success700);
  --current900: var(--success900);
}
main.incorrect {
  --current100: var(--mistake100);
  --current200: var(--mistake200);
  --current300: var(--mistake300);
  --current400: var(--mistake400);
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
  grid-template-rows: 20vh 40vh 200px;
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

#inputContainer {
  width: min(40vw, 400px);
  padding: 10px 15px;
  margin: 0;
  border: none;
  padding-right: 15px;
  height: 50px;
  background-color: var(--current100);
}
input[type="text"] {
  width: 90%;
  background: none;
  color: var(--current700);
  border: none;
  margin: 0;
  font-weight: 400;
  font-size: 2rem;
  text-align: center;
}
input[type="text"]:focus {
  outline: none !important;
  /* border: 1px solid rgb(82, 82, 161); */
}
#entered-input {
  width: 90%;
  display: inline-block;
  color: var(--current700);
  background: none;
  height: 50px;
  border: none;
  margin: 0;
  font-weight: 400;
  font-size: 2rem;
  text-align: center;
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
#inputControls {
  align-self: flex-start;
}
.buttons {
  width: 100%;
  display: flex;
  gap: 1rem;

  margin-top: 1rem;
  justify-content: center;
}
button {
  color: var(--current100);
  background-color: var(--current400);
  font-size: 1.5rem;
  border: none;

  font-weight: 400;
  border-radius: 3rem;
  padding: 0.5rem 2rem;
  padding-left: 1.5rem;
}
.hotkey {
  margin-right: 0.5rem;
  display: inline-block;
  background-color: var(--current200);
  color: var(--current500);
  width: 2.5ch;
  aspect-ratio: 1;
  border-radius: 10rem;
  font-size: 2rem;
  font-weight: 400;
}
#inputWithButton {
  display: flex;
  margin-top: 1rem;
  align-items: center;
}
#inputWithButton button {
  color: var(--current100);
  background-color: var(--current400);
  border-bottom-left-radius: 0;
  border-top-left-radius: 0;
  width: 120px;
  height: 70px;
  font-size: 1.5rem;
  padding: 0;
  font-weight: 400;
}
#inputLabel {
  font-weight: 400;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  color: var(--current400);
  font-size: 1.25rem;
  border: none;
  border-right: 1px solid var(--current200);
  border-radius: 3rem;
  background-color: var(--current200);
  border-radius: 0.5rem;
  border-bottom-right-radius: 0;
  border-top-right-radius: 0;
  width: 120px;
  height: 70px;
  padding: 10px 15px;
  padding-left: 20px;
}
</style>
