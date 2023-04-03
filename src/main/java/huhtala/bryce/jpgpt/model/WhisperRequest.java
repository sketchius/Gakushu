package huhtala.bryce.jpgpt.model;

public class WhisperRequest {
    String file;
    String model;

    public WhisperRequest(String file, String model) {
        this.file = file;
        this.model = model;
    }

    public WhisperRequest() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
