package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Library library;
    public static void main(String[] args) {
        menu();
    }
    public static void menu() {
        System.out.print(">");
        Scanner str = new Scanner(System.in);

        ArrayList<String> commands = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(str.nextLine().trim());
        while (m.find())
            commands.add(m.group(1).replace("\"", ""));

        while(commands.size()==0 || !commands.get(0).equals("exit")) {
            try {
                switch (commands.get(0)) {
                    case "create":
                        switch (commands.get(1)) {
                            case "library":
                                //create library newLibrary finePerDay
                                String name = commands.get(2);
                                double finePerDay = Double.parseDouble(commands.get(3));
                                library = new Library(name, finePerDay);
                                System.out.println("library created");
                                break;
                            case "book":
                                //create book title author page publishing year genre
                                String title = commands.get(2);
                                String author = commands.get(3);
                                int pageCount = Integer.parseInt(commands.get(4));
                                String publishingHouse = commands.get(5);
                                int year = Integer.parseInt(commands.get(6));
                                Genre genre = Genre.valueOf(commands.get(7));
                                Book book = new Book(title, author, pageCount, publishingHouse, year, genre);
                                System.out.println(library.addBook(book));
                                break;
                            case "reader":
                                //create reader Петро Петренко male 2000-06-30
                                String firstName = commands.get(2);
                                String lastName = commands.get(3);
                                Gender gender = Gender.valueOf(commands.get(4));
                                LocalDate birthday = LocalDate.parse(commands.get(5));
                                Reader reader = new Reader(firstName, lastName, gender, birthday);
                                System.out.println(library.addReader(reader));
                                break;
                            default:
                                System.out.println("Can\'t create " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "update":
                        int id = Integer.parseInt(commands.get(2));
                        switch (commands.get(1)) {
                            case "book":
                                Book book = library.getBook(id);
                                switch (commands.get(3)) {
                                    case "title":
                                        book.setTitle(commands.get(4));
                                        break;
                                    case "state":
                                        book.setState(StateBook.valueOf(commands.get(4)));
                                        break;
                                    case "genre":
                                        book.setGenre(Genre.valueOf(commands.get(4)));
                                        break;
                                    default:
                                        break;
                                }
                                System.out.println("book updated");
                                break;
                            case "reader":
                                //update reader 0 name Petro Petrenko
                                Reader reader = library.getReader(id);
                                switch (commands.get(3)) {
                                    case "name":
                                        reader.setName(commands.get(4), commands.get(5));
                                        System.out.println("name updated");
                                        break;
                                    case "payfine":
                                        //update reader 0 payfine 10
                                        System.out.println(reader.payFine(Double.parseDouble(commands.get(4))));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                System.out.println("Can\'t update " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "read":
                        switch (commands.get(1)) {
                            case "books":
                                List<Book> books = library.getBooks();
                                for (int i = 0; i < books.size(); i++) {
                                    System.out.println(books.get(i).info());
                                }
                                break;
                            case "readers":
                                List<Reader> readers = library.getReaders();
                                for (int i = 0; i < readers.size(); i++) {
                                    System.out.println(readers.get(i).info());
                                }
                                break;
                            case "book":
                                id = Integer.parseInt(commands.get(2));
                                Book book = library.getBook(id);
                                System.out.println(book.info());
                                break;
                            case "reader":
                                id = Integer.parseInt(commands.get(2));
                                Reader reader = library.getReader(id);
                                System.out.println(reader.info());
                                break;
                            case "library":
                                System.out.println(library.getName());
                                break;
                            default:
                                System.out.println("Can\'t read " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "delete":
                        id = Integer.parseInt(commands.get(2));
                        switch (commands.get(1)) {
                            case "book":
                                library.removeBook(id);
                                System.out.println("book deleted");
                                break;
                            case "reader":
                                library.removeReader(id);
                                System.out.println("reader deleted");
                                break;
                            default:
                                System.out.println("Can\'t delete " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "export":
                        FileManager fm = new FileManager();
                        switch (commands.get(1)) {
                            case "book":
                                id = Integer.parseInt(commands.get(2));
                                fm.exportBook(library.getBook(id));
                                System.out.println("book exported");
                                break;
                            case "reader":
                                id = Integer.parseInt(commands.get(2));
                                fm.exportReader(library.getReader(id));
                                System.out.println("reader exported");
                                break;
                            case "records":
                                RecordSortOptions opt = RecordSortOptions.valueOf(commands.get(2));
                                fm.exportRecord(library.getRecords(), opt, library);
                                System.out.println("records exported");
                                break;
                            case "library":
                                fm.exportLibrary(library);
                                System.out.println("library exported");
                                break;
                            default:
                                System.out.println("Can\'t export " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "import":
                        fm = new FileManager();
                        String path = commands.get(2);
                        switch (commands.get(1)) {
                            case "book":
                                Book book = fm.importBook(path);
                                library.addBook(book);
                                System.out.println("book with id "+book.getId()+" imported");
                                break;
                            case "records":
                                library.setRecords(fm.importRecords(path));
                                System.out.println("records imported");
                                break;
                            case "reader":
                                Reader reader = fm.importReader(path);
                                library.addReader(reader);
                                System.out.println("reader with id "+reader.getId()+" imported");
                                break;
                            case "library":
                                library = fm.importLibrary(path);
                                System.out.println("library imported");
                                break;
                            default:
                                System.out.println("Can\'t imported " + commands.get(1) + ". Try again");
                                break;
                        }
                        break;
                    case "borrow":
                        //borrow bookId readerId borrowDate maxReturnDate
                       int bookid = Integer.parseInt(commands.get(1));
                       int readerId = Integer.parseInt(commands.get(2));
                       LocalDate borrowDate = LocalDate.parse(commands.get(3));
                       LocalDate returnDate = LocalDate.parse(commands.get(4));
                       System.out.println(library.borrowBook(bookid,readerId,borrowDate,returnDate));
                       break;
                    case "return":
                        //return bookId returnDate
                        int bookId = Integer.parseInt(commands.get(1));
                        LocalDate returnDate1 = LocalDate.parse(commands.get(2));
                        System.out.println(library.returnBook(bookId, returnDate1));
                        break;
                    default:
                        System.out.println("Command not found. Try again");
                        break;
                }


            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
            System.out.print(">");
            commands.clear();
            m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(str.nextLine().trim());
            while (m.find())
                commands.add(m.group(1).replace("\"", ""));
        }
    }
}