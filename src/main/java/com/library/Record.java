package com.library;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Record {
    private int bookId;
    private int readerId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    public Record(int readerId,int bookId, LocalDate borrowDate, LocalDate returnDate){
        if(borrowDate.isAfter(returnDate)){
            throw new IllegalArgumentException("Borrow date must be before return Date");
        }
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate=borrowDate;
        this.returnDate=returnDate;
    }

    @JsonCreator
    public Record(@JsonProperty("bookId") int bookId,
                  @JsonProperty("borrowDate") LocalDate borrowDate,
                  @JsonProperty("returnDate") LocalDate returnDate,
                  @JsonProperty("state") StateBook state,
                  @JsonProperty("readerId") int readerId){
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.readerId = readerId;
        this.state = state;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }


    public StateBook getState() {
        return state;
    }

    public void setState(StateBook state) {
        this.state = state;
    }

    private StateBook state;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return bookId == record.bookId && readerId == record.readerId && Objects.equals(borrowDate, record.borrowDate) && Objects.equals(returnDate, record.returnDate) && state == record.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, readerId, borrowDate, returnDate, state);
    }
}
