package com.library;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.*;
import java.time.temporal.ChronoUnit;

public class Library {

    private double finePerDay;
    private List<Book> books;
    private List<Reader> readers;
    private String name;
    private ArrayList<Record> records;


    public Library(String name, double finePerDay) {
        this.name = name;
        setFinePerDay(finePerDay);
        books = new ArrayList<Book>();
        readers = new ArrayList<Reader>();
        this.records = new ArrayList<Record>();
    }
    @JsonCreator
    public Library(@JsonProperty("name") String name,
                   @JsonProperty("books") List<Book> books,
                   @JsonProperty("readers") List<Reader> readers,
                   @JsonProperty("records") ArrayList<Record> records,
                   @JsonProperty("finePerDay") double finePerDay

    ) {
        this.name = name;
        this.finePerDay = finePerDay;
        this.books = books;
        this.readers = readers;
        this.records = records;
    }


    public String addReader(Reader reader){
        if(!readers.contains(reader)){
            readers.add(reader);
            return "reader with id "+reader.getId()+" successfully added";
        }
        return "reader with id "+reader.getId()+" already exists";
    }
    public String addBook(Book book){
        if(containsBook(book.getId())){
            return "book with id "+book.getId()+" already exists";
        }
        books.add(book);
        return "book with id "+book.getId()+" successfully added";
    }

    public Book getBook(int id){
        if(containsBook(id)){
            return books.get(id);
        }
        throw new IllegalArgumentException("book not exists");
    }
    public Reader getReader(int id){
        if(containsReader(id)){
            return readers.get(id);
        }
        throw new IllegalArgumentException("book not exists");
    }
    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public double getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(double finePerDay) {
        if(finePerDay<0){
            throw new IllegalArgumentException("Fine per day must be positive");
        }
        this.finePerDay = finePerDay;
    }


    public ArrayList<Record> getRecords() {
        return records;
    }


    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public Book borrowBook(Integer readerId, Integer bookId, LocalDate borrowDate, LocalDate returnDate){
        if(!containsReader(readerId)){
            throw new IllegalArgumentException("Reader not added to library");
        }
        if(!containsBook(bookId)){
            throw new IllegalArgumentException("Book not added to library");
        }
        Reader reader = getReader(readerId);
        if(reader.getFine()>0){
            throw new IllegalArgumentException("Please pay fine: "+reader.getFine());
        }
        Book book = books.get(bookId);
        if(book.getState() == StateBook.Borrowed){
            throw new IllegalArgumentException("Book already borrowed");
        }
        book.setState(StateBook.Borrowed);
        Record rec = new Record(readerId, bookId,borrowDate,returnDate);
        records.add(rec);
        return book;
        }
    public String returnBook(Integer bookId, LocalDate returnDate) {
        if(!containsBook(bookId)){
            throw new IllegalArgumentException("Book not added to library");
        }
        Book book = getBook(bookId);
        if(book.getState()==StateBook.Returned){
            throw new IllegalArgumentException("Book already returned");
        }
        Record lastRecord = records.stream()
                .filter(record -> book.getId().equals(record.getBookId()))
                .max(Comparator.comparing(Record::getBorrowDate))
                .orElseThrow(NoSuchElementException::new);
        double fine = getFine(returnDate,lastRecord);
        Reader reader = getReader(lastRecord.getReaderId());
        reader.addFine(fine);

        book.setState(StateBook.Returned);
        if(reader.getFine()!=0){
            return "Please, pay the fine: "+reader.getFine();
        }
        return "Book successfully returned";
    }

    public void removeBook(int bookId){
        if(!containsBook(bookId)){
            throw new IllegalArgumentException("Book with id "+bookId+" not exist in this library");
        }
        Book removeBook = findBook(bookId);
        books.removeIf(book->book.getId().equals(removeBook.getId()));
    }
    public void removeReader(Integer readerId){
        readers.removeIf(reader ->readerId.equals(reader.getId()));
    }
    private double getFine(LocalDate returnDate, Record lastRecord){
        if(returnDate.isAfter(lastRecord.getReturnDate())){
            long fineDays =  ChronoUnit.DAYS.between(lastRecord.getReturnDate(), returnDate);
            return finePerDay*fineDays;
        }
        return 0;
    }
    public void updateReaderName(int readerId, String firstname, String lastname){
        readers.stream()
                .filter(reader-> reader.getId() == readerId)
                .forEach(reader -> reader.setName(firstname,lastname));
    }
    public List<Book> findBooks(String query){
        return books.stream()
                .filter(book-> book.getTitle().contains(query) ||book.getAuthor().contains(query)).toList();
    }
    private Book findBook(int idBook){
        return books.stream()
                .filter(book-> book.getId().equals(idBook))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No book found with id: "+ idBook));
    }
    private boolean containsBook(int idBook){
        boolean b = books.stream().anyMatch(e -> e.getId().equals(idBook));
        return b;
    }
    private boolean containsReader(int id){
        boolean b = readers.stream().anyMatch(e -> e.getId().equals(id));
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Double.compare(finePerDay, library.finePerDay) == 0 && Objects.equals(books, library.books) && Objects.equals(readers, library.readers) && Objects.equals(name, library.name) && Objects.equals(records, library.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finePerDay, books, readers, name, records);
    }
}
