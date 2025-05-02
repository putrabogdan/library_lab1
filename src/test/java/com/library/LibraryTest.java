package com.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LibraryTest {
    private Library library;
    private Book book;
    private Reader reader;

    @BeforeEach
    void given(){
        library = new Library("TestLibrary",5);
        book = new Book("title","author",250,"Lviv",2000, Genre.novel);
        reader = new Reader("Petro","Petrenko",Gender.male, LocalDate.parse("2000-02-01"));
    }
    @Test
    void testFine(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        library.returnBook(0,LocalDate.parse("2025-02-11"));
        assertEquals(50,library.getReader(0).getFine());
    }
    @Test
    void testGetReaderNotAddedToLibraryException(){
        assertThrows(IllegalArgumentException.class,()->library.getReader(-5));
    }
    @Test
    void testGetBookNotAddedToLibraryException(){
        assertThrows(IllegalArgumentException.class,()->library.getBook(-5));
    }

    @Test
    void testUpdateBook(){
        library.addBook(book);
        library.getBook(0).setTitle("new title");
        assertEquals("new title",library.getBook(0).getTitle());
    }

    @Test
    void testUpdateReader(){
        library.addReader(reader);
        library.getReader(0).setName("Ivan", "Ivanov");
        assertEquals("Ivan",library.getReader(0).getFirstName());
        assertEquals("Ivanov",library.getReader(0).getLastName());
    }

    @Test
    void testInvalidReaderBirthday(){
        assertThrows(IllegalArgumentException.class,()->new Reader("Anna","Petrenko",Gender.male, LocalDate.parse("2222-02-01")));
    }
    @Test
    void testBorrowWithFine(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        library.returnBook(0,LocalDate.parse("2025-02-11"));
        assertThrows(IllegalArgumentException.class,()->library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01")));
    }
    @Test
    void testPayFine(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        library.returnBook(0,LocalDate.parse("2025-02-11"));
        library.getReader(0).payFine(10);
        assertEquals(40,library.getReader(0).getFine());
    }
    @Test
    void testBorrowBookStatus(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        assertEquals(StateBook.Borrowed,library.getBook(0).getState());
    }
    @Test
    void testReturnedBookStatus(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        library.returnBook(0,LocalDate.parse("2025-01-11"));
        assertEquals(StateBook.Returned,library.getBook(0).getState());
    }
    @Test
    void testRecord(){
        library.addBook(book);
        library.addReader(reader);
        library.borrowBook(0,0,LocalDate.parse("2025-01-01"),LocalDate.parse("2025-02-01"));
        assertEquals(0,library.getRecords().get(0).getBookId());
        assertEquals(0,library.getRecords().get(0).getReaderId());
        assertEquals(LocalDate.parse("2025-01-01"),library.getRecords().get(0).getBorrowDate());
        assertEquals(LocalDate.parse("2025-02-01"),library.getRecords().get(0).getReturnDate());

    }

    @Test
    void testExportLibrary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        library.addBook(book);
        library.addReader(reader);
        FileManager fm = new FileManager();

        fm.exportData(library, "testLibrary");
        String json = new String(Files.readAllBytes(Paths.get("testLibrary.json")));
        Library exportedLibrary = mapper.readValue(json, Library.class);
        assertEquals("title",exportedLibrary.getBook(0).getTitle());
        assertEquals("author",exportedLibrary.getBook(0).getAuthor());
        assertEquals(250,exportedLibrary.getBook(0).getPageCount());
        assertEquals("Lviv",exportedLibrary.getBook(0).getPublishing());
        assertEquals(2000,exportedLibrary.getBook(0).getYear());
        assertEquals(Genre.novel,exportedLibrary.getBook(0).getGenre());
        assertEquals("Petro",exportedLibrary.getReader(0).getFirstName());
        assertEquals("Petrenko",exportedLibrary.getReader(0).getLastName());
    }

    @Test
    void testImportLibrary() throws  IOException{
        String json = "{\"name\":\"TestLibrary\",\"books\":[{\"title\":\"title\",\"author\":\"author\",\"pageCount\":250,\"genre\":\"novel\",\"publishing\":\"Lviv\",\"year\":2000,\"state\":\"Returned\",\"id\":0}],\"readers\":[{\"firstName\":\"Petro\",\"lastName\":\"Petrenko\",\"gender\":\"male\",\"birthday\":[2000,2,1],\"id\":0,\"fine\":0.0}],\"records\":[],\"finePerDay\":5.0}";
        FileManager fm2 = mock(FileManager.class);
        when(fm2.jsonFromFile(Paths.get("filepath"))).thenReturn(json);
        FileManager fm1 = new FileManager();
        String jsonLibrary = fm2.jsonFromFile(Paths.get("filepath"));
        Library importLibrary = fm1.jsonToLibrary(jsonLibrary.getBytes());

        assertEquals("title",importLibrary.getBook(0).getTitle());
        assertEquals("author",importLibrary.getBook(0).getAuthor());
        assertEquals(250,importLibrary.getBook(0).getPageCount());
        assertEquals("Lviv",importLibrary.getBook(0).getPublishing());
        assertEquals(2000,importLibrary.getBook(0).getYear());
        assertEquals(Genre.novel,importLibrary.getBook(0).getGenre());
        assertEquals("Petro",importLibrary.getReader(0).getFirstName());
        assertEquals("Petrenko",importLibrary.getReader(0).getLastName());
    }
}