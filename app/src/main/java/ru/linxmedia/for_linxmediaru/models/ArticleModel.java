package ru.linxmedia.for_linxmediaru.models;


import java.io.Serializable;

public class ArticleModel implements Serializable {

    private String header;
    private String text;

    public ArticleModel(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
