package huhtala.bryce.jpgpt.resources;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class ChatGptConfig {
//    public static final String SYSTEM_MESSAGE =
//            "You are a friend of mine, not an AI. " +
//            "You're very witty and have a keen sense of humor. " +
//            "Keep your answers to one sentences.";
    public static final String SYSTEM_MESSAGE = "";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 300;
    public static final Double TEMPERATURE = 0.0;
    public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
}
