<template>
  <span class="loader"></span>
</template>

<script>
export default {
  name: "animated-loader",
};
</script>

<style>
.loader {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: block;
  margin: 15px auto;
  position: relative;
  background: #fff;
  box-sizing: border-box;
  animation: shadowExpandX 2s linear infinite alternate;
}

@keyframes shadowExpandX {
  0% {
    box-shadow: 0 0, 0 0;
    color: rgba(var(--current400), 0.2);
  }
  100% {
    box-shadow: -24px 0, 24px 0;
    color: rgba(var(--current100), 0.8);
  }
}
</style>
