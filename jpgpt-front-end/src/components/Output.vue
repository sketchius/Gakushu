<template>
  <div id="display">
    <h2 id="output">
      <!-- <div lang="ja" class="japanese">{{ outputJapanese }}</div> -->
      <div id="output" class="english">{{ output }}</div>
    </h2>
    <div id="content-container" :class="[contentDisplay]">
      <div
        id="content"
        :class="[contentDisplay, contentLanguage]"
        translate="no"
      >
        {{ content }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "output-component",
  props: [
    "output",
    "outputContext",
    "content",
    "contentDisplay",
    "contentLanguage",
  ],
};
</script>
<style>
#display {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 1fr 2fr;
  align-items: center;
  justify-items: center;
  margin-bottom: 2rem;
}
#output {
  white-space: pre-line;
  color: rgb(var(--current400));
  font-weight: 400;
  font-size: 3rem;
  margin: 0;
  align-self: center;
}

#content-container {
  align-self: center;
  margin-top: 1rem;
}
#content-container.normal {
  background: radial-gradient(
    circle at center,
    rgba(var(--current100), 1),
    rgba(var(--current200), 0.5)
  );
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  border: 5px rgb(var(--current300)) double;
  border-radius: 5rem;
  width: fit-content;
  height: fit-content;
  padding: 0 2.5rem;
}

#content-container.compact {
  background: radial-gradient(
    circle at center,
    rgba(var(--current100), 1),
    rgba(var(--current500), 0)
  );
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  border: 7px rgb(var(--current300)) double;
  border-radius: 50rem;
  width: 15rem;
  height: 15rem;
}

#content-container.long {
  background: radial-gradient(
    circle at center,
    rgb(var(--current100)),
    rgba(255, 255, 255, 0)
  );
  display: flex;
  align-items: center;
  justify-content: center;
  vertical-align: center;
  border: 5px rgb(var(--current300)) double;
  border-radius: 2.5rem;
  width: fit-content;
  height: fit-content;
  padding: 1rem 2.5rem;
}

#content.normal {
  font-weight: 300;
  margin: 0;
  font-size: 5rem;
  color: rgb(var(--current500));
  position: relative;
  bottom: 0.5rem;
}

#content.compact {
  margin: 0;
  font-size: 7.5rem;
  color: rgb(var(--current500));
  position: relative;
  bottom: 0.5rem;
}

#content.long {
  position: relative;
  margin: 0;
  font-size: 3.5rem;
  color: rgb(var(--current400));
  bottom: 0.5rem;
}

.english {
  font-family: "Noto Sans", sans-serif;
  font-weight: 400;
}

.japanse {
  font-family: "Noto Sans JP", sans-serif;
}
</style>
