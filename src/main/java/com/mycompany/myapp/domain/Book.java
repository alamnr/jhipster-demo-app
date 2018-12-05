package com.mycompany.myapp.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Book {

    private long id;
    private String isbn;
    private String title;
    private Publisher publisher;
    private List<Chapter> chapters;

    public Book() {
    }

    public Book(String isbn, String title, Publisher publisher) {
        this.isbn = isbn;
        this.title = title;
        this.publisher = publisher;
        this.chapters = new ArrayList<Chapter>();
    }

    public void addChapter(Chapter chapter){
        this.chapters.add(chapter);
    }

    public Iterator<Chapter> getChapterIterator(){
        return this.chapters.iterator();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", isbn='" + isbn + '\'' +
            ", title='" + title + '\'' +
            ", publisher=" + publisher +
            ", chapters=" + chapters +
            '}';
    }
}
