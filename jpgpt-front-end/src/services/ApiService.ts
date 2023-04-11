import axios from "axios";

const http = axios.create({
  baseURL: "http://localhost:8080",
});

export default {
  list() {
    return http.get("/users");
  },

  requestChallenge() {
    return http.get("/api/item");
  },

  requestQuestionChallenge() {
    return http.get("/api/question/random");
  },

  getHiragana(itemId: number, type: string) {
    return http.get(`/api/item/${itemId}/hiragana?type=${type}`);
  },

  getAnswer(itemId: number, type: string) {
    return http.get(`/api/item/${itemId}/answer?type=${type}`);
  },

  checkAnswer(itemId: number, type: string, input: string) {
    return http.post(`api/item/${itemId}?type=${type}&input=${input}`);
  },

  checkQuestionAnswer(itemId: number, type: string, input: string) {
    return http.post(`api/question/${itemId}?type=${type}&input=${input}`);
  },

  appealAnswer(itemId: number, answer: string) {
    return http.post(`api/kanji/${itemId}?answer=${answer}&appeal=true`);
  },
};
