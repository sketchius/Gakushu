package huhtala.bryce.jpgpt.model;

import java.io.Serializable;


public class BotRequest implements Serializable {
    private String message;

    public BotRequest(String message) {
        this.message = message;
    }

    public BotRequest() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
