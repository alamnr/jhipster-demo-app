package com.mycompany.myapp.domain;

public class Chapter {

    private String title;
    private int chapterNumber;

    public Chapter() {
    }

    public Chapter(String title, int chapterNumber) {
        this.title = title;
        this.chapterNumber = chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    @Override
    public String toString() {
        return "Chapter{" +
            "title='" + title + '\'' +
            ", chapterNumber=" + chapterNumber +
            '}';
    }
}
