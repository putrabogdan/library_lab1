package com.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileManager {
    ObjectMapper mapper;
    public FileManager() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    public void exportData(Object obj, String filename) {
        try {
            File file = new File(filename + ".json");
            mapper.writeValue(file, obj);
            System.out.println("Filed saved to: " + file.getAbsolutePath());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportBook(Book book) {
        exportData(book, "book_" + book.getId());
    }

    public void exportReader(Reader reader) {
        exportData(reader, "reader_" + reader.getId());
    }
    public void exportRecords(List<Record> records) {
        exportData(records, "records" );
    }
    public void exportLibrary(Library library) {
        exportData(library, "library");
    }
    public void exportRecord(List<Record> records, RecordSortOptions sortOpt, Library library) {
        switch (sortOpt){
            case ByGenre:
                records.sort(Comparator.comparing(o -> library.getBook(o.getBookId()).getGenre()));
                break;
            case ByBookId:
                records.sort(Comparator.comparing(o -> o.getBookId()));
                break;
            case ByReaderId:
                records.sort(Comparator.comparing(o -> o.getReaderId()));
                break;
            case ByBorrowDate:
                records.sort(Comparator.comparing(o -> o.getBorrowDate()));
                break;
            case ByState:
                records.sort(Comparator.comparing(o -> o.getState()));
                break;
        }
        exportData(records, "records");
    }
    public Library importLibrary(String filepath) {
        try {
            String json = jsonFromFile(Paths.get(filepath));
            Library library = mapper.readValue(json, Library.class);
            return library;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Library jsonToLibrary(byte[] json) throws JsonProcessingException {
        Library library = mapper.readValue(new String(json), Library.class);
        return library;
    }
    public String jsonFromFile(Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
    public Book importBook (String filepath){
        try {
            String json = jsonFromFile(Paths.get(filepath));
            Book book = mapper.readValue(json, Book.class);
            return book;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<Record> importRecords (String filepath){
        try {
            String json = jsonFromFile(Paths.get(filepath));
            ArrayList<Record> records = mapper.readValue(json, ArrayList.class);
            return records;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Reader importReader (String filepath){
        try {
            String json = jsonFromFile(Paths.get(filepath));
            Reader reader = mapper.readValue(json, Reader.class);
            return reader;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

