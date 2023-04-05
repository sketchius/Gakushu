package huhtala.bryce.jpgpt.model;

public class QuizDTO {
    String content;
    String output;
    String outputContext;
    String topic;

    public QuizDTO(String content, String output, String outputContext, String topic) {
        this.content = content;
        this.output = output;
        this.outputContext = outputContext;
        this.topic = topic;
    }

    public QuizDTO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutputContext() {
        return outputContext;
    }

    public void setOutputContext(String outputContext) {
        this.outputContext = outputContext;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
