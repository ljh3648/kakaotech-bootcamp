package kr.adapterz.springboot.dto;

public class Text {
    private String title;
    private String message;

    public Text(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
