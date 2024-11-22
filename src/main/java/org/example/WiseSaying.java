package org.example;

public class WiseSaying {

    private int id;
    private String author;
    private String wiseSaying;

    public WiseSaying(int id, String author, String wiseSaying) {
        this.id = id;
        this.author = author;
        this.wiseSaying = wiseSaying;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWiseSaying() {
        return wiseSaying;
    }

    public void setWiseSaying(String wiseSaying) {
        this.wiseSaying = wiseSaying;
    }
}
