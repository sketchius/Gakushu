<template>
  <main
    :class="[
      displayMode == 'prompt' ? 'prompt' : correct ? 'success' : 'mistake',
    ]"
  >
    <h1>{{ this.topic }}</h1>
    <output-component
      v-bind:output="this.output"
      v-bind:content="this.content"
      v-bind:content-display="this.contentDisplay"
      v-bind:content-language="this.contentLanguage"
    />

    <section id="controls">
      <div id="inputWithButton">
        <label id="inputLabel">Response:</label>
        <div id="inputContainer">
          <input
            autocomplete="off"
            id="textInput"
            ref="inputField"
            v-model="input"
            v-show="displayMode == 'prompt'"
            v-on="handleInput()"
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
          <span v-if="!this.loading">Send</span>
          <animated-loader v-if="this.loading" />
        </button>
      </div>

      <div class="buttons">
        <button
          v-on:click="handleHint()"
          v-show="displayMode == 'prompt' && mode != 'hint'"
        >
          <p class="hotkey">F1</p>
          <p class="text">Hint</p>
        </button>
        <button
          v-on:click.prevent="handleHiragana()"
          v-show="displayMode == 'prompt' && showHiraganaToggle == true"
        >
          <p class="hotkey">F2</p>
          <p class="text">{{ kanjiOrHiragana }}</p>
        </button>
        <button v-on:click="handleTryAgain()" v-show="mode == 'incorrect'">
          <p class="hotkey">T</p>
          <p class="text">Try Again</p>
        </button>
        <button v-on:click="handleShowAnswer()" v-show="mode == 'incorrect'">
          <p class="hotkey">A</p>
          <p class="text">See Answer</p>
        </button>
        <!-- <button v-on:click="handleAppealAnswer()" v-show="mode == 'showAnswer'">
          <p class="hotkey">E</p>
          <p class="text">Appeal Evaluation</p>
        </button> -->
      </div>
    </section>
  </main>
</template>

<script>
import apiService from "../services/ApiService.ts";
import outputComponent from "../components/Output.vue";
import animatedLoader from "../components/AnimatedLoader.vue";

import * as wanakana from "wanakana";

export default {
  name: "study-view",
  data() {
    return {
      loading: true,
      displayMode: "prompt",
      mode: "",
      input: "",
      englishInput: true,
      output: "",
      content: "",
      contentType: "",
      contentLanguage: "Japanese",
      contentId: 0,
      topic: "",
      contentDisplay: "normal",
      correct: false,
      showHiraganaToggle: false,
      showHiragana: false,
      kanjiOrHiragana: "Show Hiragana",
    };
  },
  methods: {
    processAnswer() {
      debugger;
      if (this.displayMode == "prompt") {
        apiService
          .checkAnswer(this.contentId, this.contentType, this.input)
          .then((response) => {
            const evaluation = response.data;
            if (evaluation) {
              // this.outputJapanese = "せいかい";
              this.output = "Correct!";
              this.correct = true;
              this.displayMode = "result";
              this.mode = "correct";
            } else {
              // this.outputJapanese = "ちがいです";
              this.output = "Sorry, that's not right.";
              this.correct = false;
              this.displayMode = "result";
              this.mode = "incorrect";
            }
          });
      } else {
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
      console.log("getNewChallenge()");
      this.setLoading();
      apiService.requestChallenge().then((response) => {
        const data = response.data;
        this.content = data.content;
        this.contentId = data.id;
        this.contentType = data.type;
        this.showHiraganaToggle = false;
        this.showHiragana = false;
        this.kanjiOrHiragana = "Hiragana";
        let useHiragana = false;
        console.log(data);
        switch (data.topic) {
          case "Kanji":
            switch (data.type) {
              case "meaning":
                this.output = "What is the meaning of this character?";
                this.contentLanguage = "japanese";
                this.englishInput = true;
                break;
              case "kunreading":
                this.output = "What is the Kun'Yomi reading of this character?";
                this.contentLanguage = "japanese";
                this.englishInput = false;
                break;
              case "onreading":
                this.output = "What is the On'Yomi reading of this character?";
                this.contentLanguage = "japanese";
                this.englishInput = false;
                break;
            }
            break;
          case "Vocab":
            switch (data.type) {
              case "e2j":
                this.output = "How do you say this in Japanese?";
                this.contentLanguage = "english";
                this.englishInput = false;
                break;
              case "j2e":
                this.output = "What does this mean in English?";
                this.contentLanguage = "japanese";
                this.englishInput = true;
                useHiragana = true;
                break;
            }
            break;
          case "Conversation":
            switch (data.type) {
              case "e2j":
                this.output = "How do you say this in Japanese?";
                this.englishInput = false;
                this.contentLanguage = "english";
                break;
              case "j2e":
                this.output = "What does this mean in English?";
                this.englishInput = true;
                this.contentLanguage = "japanese";
                useHiragana = true;
                break;
              case "question":
                this.output = "How would you respond to this in Japanese?";
                this.englishInput = false;
                this.contentLanguage = "japanese";
                useHiragana = true;
                break;
            }
            break;
        }

        this.currentId = data.id;
        this.displayMode = "prompt";
        this.mode = "prompt";
        this.topic = data.topic;
        this.loading = false;
        if (
          useHiragana &&
          data.altContent &&
          !wanakana.isHiragana(data.content) &&
          !wanakana.isKatakana(data.content)
        ) {
          this.showHiraganaToggle = true;
          this.altContent = data.altContent;
        }
        // if (this.englishInput) {
        //   if (this.wanaKanaBound) {
        //     wanakana.unbind(this.inputElement);
        //     this.wanakanaBound = false;
        //   }
        // } else {
        //   wanakana.bind(this.inputElement);
        //   this.wanakanaBound = true;
        // }
        this.updateContentDisplay();
        this.focusInputField();
      });
    },
    setLoading() {
      this.loading = true;
      this.displayMode = "prompt";
      this.output = "Loading Content...";
      this.input = "";
      this.mode = "prompt";
      this.correct = false;
      this.topic = "";
    },
    updateContentDisplay() {
      if (this.content.length == 1) {
        this.contentDisplay = "compact";
      } else if (this.content.length < 4) {
        this.contentDisplay = "normal";
      } else {
        this.contentDisplay = "long";
      }
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
    handleHiragana() {
      const currContent = this.content;
      this.content = this.altContent;
      this.altContent = currContent;
      this.showHiragana = !this.showHiragana;
      if (this.showHiragana) this.kanjiOrHiragana = "Show Kanji";
      else this.kanjiOrHiragana = "Show Hiragana";
      this.updateContentDisplay();
    },
    handleSubmit() {
      debugger;
      if (this.input != "") this.processAnswer();
    },
    handleTryAgain() {
      this.setPrompt();
      this.focusInputField();
    },
    handleInput() {
      if (!this.englishInput) {
        this.input = wanakana.toHiragana(this.input, { IMEMode: true });
      }
    },
    handleShowAnswer() {
      apiService
        .getAnswer(this.contentId, this.contentType)
        .then((response) => {
          this.mode = "showAnswer";
          this.outputJapanese = "";
          const multipleAnswers = response.data.length > 1;
          const answers = response.data.join(", ");
          this.output = `The correct ${
            multipleAnswers ? "answers are" : "answer is"
          }:\n${answers}`;
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
    self.getNewChallenge();
    self.focusInputField();

    const textInput = document.getElementById("textInput");

    textInput.addEventListener("keydown", (event) => {
      if (event.key == "`" || event.key == "~") {
        event.preventDefault();
      }
    });

    self.inputElement = textInput;

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
        case "F2":
          if (self.mode == "prompt" && self.showHiraganaToggle == true)
            self.handleHiragana();
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
    animatedLoader,
  },
};
</script>

<style scoped>
* {
  font-family: "Noto Sans", sans-serif;
  font-weight: 300;
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
main.success {
  --current100: var(--success100);
  --current200: var(--success200);
  --current300: var(--success300);
  --current400: var(--success400);
  --current500: var(--success500);
  --current700: var(--success700);
  --current900: var(--success900);
}
main.mistake {
  --current100: var(--mistake100);
  --current200: var(--mistake200);
  --current300: var(--mistake300);
  --current400: var(--mistake400);
  --current500: var(--mistake500);
  --current700: var(--mistake700);
  --current900: var(--mistake900);
}
h1 {
  display: block;
  width: 100%;
  opacity: 100%;
  color: rgb(var(--current100));
  /* color: white; */
  font-weight: 400;
  font-size: 5rem;
  margin: 2rem;
  padding-bottom: 1rem;
  letter-spacing: 0.5rem;
  /* text-transform: uppercase; */
  background: linear-gradient(
    to right,
    rgba(var(--current200), 0),
    rgba(var(--current300), 0.75) 25%,
    rgba(var(--current300), 0.75) 50%,
    rgba(var(--current300), 0.75) 75%,
    rgba(var(--current200), 0)
  );
  line-height: 6.5rem;
}
.container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  background: linear-gradient(
    to top,
    rgb(var(--current300)),
    rgb(var(--current100))
  );
}

main {
  background: linear-gradient(
    to top,
    rgb(var(--current300)),
    rgb(var(--current100))
  );
  height: 100vh;
  width: 100vw;
  margin: 0;
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: min-content 1.5fr 1fr;
  align-items: center;
  justify-items: center;
}
.grid {
  width: 100%;
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 20vh 40vh 200px;
  align-items: center;
  justify-items: center;
}
#content-container {
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  border: 5px rgb(var(--current300)) double;
  border-radius: 50rem;
  width: 15rem;
  height: 15rem;
}
#inputContainer {
  width: min(40vw, 400px);
  padding: 10px 15px;
  margin: 0;
  border: none;
  padding-right: 15px;
  height: 50px;
  background-color: rgb(var(--current100));
}
input[type="text"] {
  width: 90%;
  background: none;
  color: rgb(var(--current700));
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
  color: rgb(var(--current700));
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
  font-family: "Noto Sans JP", serif;
  line-height: 100%;
}
#controls {
  display: grid;
  grid-template-rows: 1fr 2fr;
  align-self: center;
}
.buttons {
  width: 100%;
  display: flex;
  gap: 1rem;

  margin-top: 1rem;
  justify-content: center;
}
.buttons button {
  display: flex;
  flex-direction: column;
  position: relative;
  background-color: rgb(var(--current400));
  border: none;
  height: 6.5rem;
  width: max-content;
  font-weight: 400;
  border-radius: 100rem;
  align-items: center;
}
.buttons .text {
  margin: 0 1rem;
  color: rgb(var(--current100));
  font-size: 1.5rem;
}
.hotkey {
  display: inline-block;
  color: rgb(var(--current200));
  border-radius: 10rem;
  font-size: 2.5rem;
  margin: 0;
  font-weight: 400;
}
#inputWithButton {
  display: flex;
  margin-top: 1rem;
  align-items: center;
}
#inputWithButton button {
  color: rgb(var(--current100));
  background-color: rgb(var(--current400));
  border-style: none;
  border-radius: 10rem;
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
  color: rgb(var(--current400));
  font-size: 1.25rem;
  border: none;
  border-right: 1px solid rgb(var(--current200));
  border-radius: 3rem;
  background-color: rgb(var(--current200));
  border-radius: 0.5rem;
  border-bottom-right-radius: 0;
  border-top-right-radius: 0;
  width: 120px;
  height: 70px;
  padding: 10px 15px;
  padding-left: 20px;
  margin-right: 0.3rem;
}
</style>
