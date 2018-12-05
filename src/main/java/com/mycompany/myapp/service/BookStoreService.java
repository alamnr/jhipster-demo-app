package com.mycompany.myapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mycompany.myapp.domain.Book;
import com.mycompany.myapp.domain.Chapter;
import com.mycompany.myapp.domain.Publisher;


public class BookStoreService {

    private Connection connection = null;

    public void persistObjectGraph(Book book) throws Exception{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "percy123");
            System.out.println(connection.getClass());
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO PUBLISHER (CODE, PUBLISHER_NAME) VALUES (?, ?)");
            stmt.setString(1, book.getPublisher().getCode());
            stmt.setString(2, book.getPublisher().getName());
            stmt.executeUpdate();

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO BOOK (ISBN, BOOK_NAME, PUBLISHER_CODE) VALUES (?, ?, ?)");
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getPublisher().getCode());
            stmt.executeUpdate();

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO CHAPTER (BOOK_ISBN, CHAPTER_NUM, TITLE) VALUES (?, ?, ?)");
           /* for(Chapter chapter: book.getChapters()) {
                stmt.setString(1, book.getIsbn());
                stmt.setInt(2, chapter.getChapterNumber());
                stmt.setString(3, chapter.getTitle());
                stmt.executeUpdate();
            }*/
            Iterator<Chapter> iterator  =  book.getChapterIterator();
            while(iterator.hasNext()){
                Chapter chapter  = iterator.next();
                stmt.setString(1, book.getIsbn());
                stmt.setInt(2, chapter.getChapterNumber());
                stmt.setString(3, chapter.getTitle());
                stmt.executeUpdate();
            }


            stmt.close();
        }
        catch (ClassNotFoundException e) {
            // e.printStackTrace();
            throw new Exception( "Class not fount Exception");
        }
        catch (SQLException e) {
            // e.printStackTrace();
            throw new Exception( "SQL Exception");
        }
        catch(Exception e){
            throw new Exception( "Unkown Exception");
        }
        finally {
            try {
                connection.close();
            }
            catch (SQLException e) {
                // e.printStackTrace();
                throw new Exception( "SQL Can't find connexion to close");
            }
        }
    }

    public Book retrieveObjectGraph(String isbn) {
        Book book = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "percy123");

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BOOK, PUBLISHER WHERE BOOK.PUBLISHER_CODE = PUBLISHER.CODE AND BOOK.ISBN = ?");
            stmt.setString(1, isbn);

            ResultSet rs = stmt.executeQuery();

            book = new Book();
            if (rs.next()) {
                book.setIsbn(rs.getString("ISBN"));
                book.setTitle(rs.getString("BOOK_NAME"));

                Publisher publisher = new Publisher();
                publisher.setCode(rs.getString("CODE"));
                publisher.setName(rs.getString("PUBLISHER_NAME"));
                book.setPublisher(publisher);
            }

            rs.close();
            stmt.close();

           // List<Chapter> chapters = new ArrayList<Chapter>();
            stmt = connection.prepareStatement("SELECT * FROM CHAPTER WHERE BOOK_ISBN = ?");
            stmt.setString(1, isbn);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setTitle(rs.getString("TITLE"));
                chapter.setChapterNumber(rs.getInt("CHAPTER_NUM"));
                //chapters.add(chapter);
                book.addChapter(chapter);
            }
            //book.setChapters(chapters);

            rs.close();
            stmt.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return book;
    }

}
