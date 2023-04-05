<template translate="no">
  <!-- <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link>
  </nav> -->
  <router-view />
</template>

<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans:wght@300;400&family=Noto+Sans+JP:wght@300;400&family=Noto+Serif+JP&display=swap");
body {
  margin: 0;

  --primary100: 241, 251, 255;
  --primary200: 210, 233, 255;
  --primary300: 129, 145, 218;
  --primary400: 101, 109, 221;
  --primary500: 75, 68, 177;
  --primary700: 53, 39, 116;
  --primary900: 29, 17, 56;
  --success100: 241, 253, 255;
  --success200: 176, 246, 240;
  --success300: 129, 218, 211;
  --success400: 81, 195, 171;
  --success500: 68, 177, 153;
  --success700: 39, 116, 97;
  --success900: 17, 56, 40;
  --mistake100: 255, 241, 244;
  --mistake200: 255, 210, 227;
  --mistake300: 218, 129, 171;
  --mistake400: 211, 96, 167;
  --mistake500: 177, 68, 141;
  --mistake700: 116, 39, 99;
  --accent100: 255, 254, 241;
  --accent200: 254, 255, 210;
  --accent300: 218, 217, 129;
  --mistake400: 211, 96, 167;
  --mistake500: 177, 68, 141;
  --mistake700: 116, 39, 99;
  --primaryhighlight: rgb(82, 143, 255);

  --primaryhighlighter: rgb(76, 93, 243);
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
