package huhtala.bryce.jpgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Choice implements Serializable {
    private Message message;
    private Integer index;
    @JsonProperty("finish_reason")
    private String finishReason;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
}

