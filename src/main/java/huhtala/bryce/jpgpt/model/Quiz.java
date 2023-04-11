package huhtala.bryce.jpgpt.model;

public class Quiz {
    int id;
    String content;
    String altContent;
    String type;
    String topic;

    public Quiz(int id, String content, String altContent, String type, String topic) {
        this.id = id;
        this.content = content;
        this.altContent = altContent;
        this.type = type;
        this.topic = topic;
    }

    public Quiz() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAltContent() {
        return altContent;
    }

    public void setAltContent(String altContent) {
        this.altContent = altContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
