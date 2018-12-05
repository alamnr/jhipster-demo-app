package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.Chapter;
import com.mycompany.myapp.domain.Publisher;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BookStoreClientTest {

    @Test
    public void testBookStoreClient() throws Exception {

            BookStoreService bookStoreService = new BookStoreService();

            //persisting object graph

			Publisher publisher = new Publisher("MANN", "Manning Publications Co.");
			Book book = new Book("9781617290459", "Java Persistence with Hibernate, Second Edition", publisher);
			List<Chapter> chapters = new ArrayList<Chapter>();
			Chapter chapter1 = new Chapter("Introducing JPA and Hibernate", 1);
			chapters.add(chapter1);
			Chapter chapter2 = new Chapter("Domain Models and Metadata", 2);
			chapters.add(chapter2);
			//book.setChapters(chapters);
            book.addChapter(chapter2);
			bookStoreService.persistObjectGraph(book);

            //retrieving object graph
            /* */
            Book bookRetrieve = bookStoreService.retrieveObjectGraph("9781617290459");
            System.out.println(bookRetrieve);
            Assertions.assertThat(bookRetrieve).isNotNull();
            /* */

    }
}
