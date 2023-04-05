import axios from "axios";

const http = axios.create({
  baseURL: "http://localhost:8080",
});

export default {
  list() {
    return http.get("/users");
  },

  requestChallenge() {
    return http.get("/api/sets/1/random");
  },

  requestQuestionChallenge() {
    return http.get("/api/question/random");
  },

  getMeaning(itemId: number) {
    return http.get(`/api/kanji/${itemId}/meaning`);
  },

  checkAnswer(itemId: number, answer: string) {
    return http.post(`api/kanji/${itemId}?answer=${answer}`);
  },

  checkQuestionAnswer(itemId: number, answer: string) {
    return http.post(`api/question/${itemId}?answer=${answer}`);
  },

  appealAnswer(itemId: number, answer: string) {
    return http.post(`api/kanji/${itemId}?answer=${answer}&appeal=true`);
  },
};
