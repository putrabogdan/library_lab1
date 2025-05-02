package com.library;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Book {

    private String title;
    private final String author;
    private final int pageCount;
    private Genre genre;
    private final String publishing;
    private final int year;
    private StateBook state;
    private static int idCount=0;
    private final int id;

    public Book(final String title, final String author,final int pageCount,final String publishingHouse, final int year, Genre genre){
        this.author = author;
        if(pageCount <= 0){
            throw new IllegalArgumentException("Page count less than 0");
        }
        if(year > LocalDate.now().getYear()){
            throw new IllegalArgumentException("Year of creation of book can not be greater than current");
        }
        this.year = year;
        this.pageCount = pageCount;
        this.publishing = publishingHouse;
        this.title = title;
        this.state = StateBook.Returned;
        this.genre = genre;
        this.id = idCount;
        idCount++;
    }
    @JsonCreator
    public Book(@JsonProperty("title") String title,
                @JsonProperty("author") String author,
                @JsonProperty("pageCount") int pageCount,
                @JsonProperty("genre") Genre genre,
                @JsonProperty("publishing") String publishing,
                @JsonProperty("year") int year,
                @JsonProperty("state") StateBook state,
                @JsonProperty("id") int id){

        if(pageCount <= 0){
            throw new IllegalArgumentException("Page count less than 0");
        }
        if(year > LocalDate.now().getYear()){
            throw new IllegalArgumentException("Year of creation of book can not be greater than current");
        }
        this.author = author;
        this.year = year;
        this.pageCount = pageCount;
        this.publishing = publishing;
        this.title = title;
        this.state = StateBook.Returned;
        this.genre = genre;
        this.id = id;
        this.state = state;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    public Genre getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }
    public Integer getId() {
        return id;
    }
    public StateBook getState() {
        return state;
    }

    public void setState(StateBook state) {
        this.state = state;
    }

    public String getPublishing() {
        return publishing;
    }




    public String info() {
        return "Book:" +"\n" +
                "id: " + id+"\n" +
                "title:" + title + "\n" +
                "author:" + author + "\n" +
                "page count:" + pageCount +"\n" +
                "genre: " + genre +"\n" +
                "publishing: " + publishing + "\n" +
                "year: " + year +"\n" +
                "state: " + state;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String toString() {
        return info();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return pageCount == book.pageCount && year == book.year && id == book.id && Objects.equals(title, book.title) && Objects.equals(author, book.author) && genre == book.genre && Objects.equals(publishing, book.publishing) && state == book.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, pageCount, genre, publishing, year, state, id);
    }
}
