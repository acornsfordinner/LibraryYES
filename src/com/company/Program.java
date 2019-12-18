package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Program {
    final Path pathBooks = Paths.get("Books.txt");
    final Path pathBorrowedBooks = Paths.get("BorrowedBooks.txt");
    final Path pathAvailibleBooks = Paths.get("AvailibleBooks.txt");
    List<String> bookList = getBooks(pathBooks);
    List<String> bookListBorrowed = getBooks(pathBorrowedBooks);
    List<String> bookListAvailible = getBooks(pathAvailibleBooks);


    Program() {
        System.out.println("Welcome to the library, chose what you want to do");
        System.out.println("1: View books in the library");
        System.out.println("2: Borrow a book");
        System.out.println("3: Return a book");
        System.out.println("4: View which books are availible to borrow");

        Scanner scan = new Scanner(System.in);
        int number = scan.nextInt();
        switch (number) {
            case 1: {
                printAllBooks(bookList);
                break;
            }
            case 2: {
                borrowBook();
                break;
            }
            case 3: {
                returnBook();
                break;
            }
            case 4: {
                printAllBooks(bookListAvailible);
            }
            default:

        }
    }

    private void borrowBook() {
        String answer;
        int matches = 0;
        String matchedBook = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search for a book");
        String bookSearch = scanner.nextLine();
        for (String book : bookListAvailible) {

            if (book.toLowerCase().contains(bookSearch.toLowerCase())) {
                System.out.println(book);
                matchedBook = book;
                matches++;

            }
        }

        if (matches > 1) {
            System.out.println("Too many matches, be more specific");
            borrowBook();

        }
        if (matches == 1) {
            do {
                System.out.println("Do you want to borrow this book?");
                answer = scanner.nextLine();
                if (answer.toLowerCase().equals("no")) {
                    borrowBook();
                } else if (answer.toLowerCase().equals("yes")) {
                    //lägg till matchedBook till borrowedBooks
                    String addToBorrowed = "";
                    bookListBorrowed.add(matchedBook);

                    for (String item : bookListBorrowed){
                        addToBorrowed = addToBorrowed.concat(item)+"\n";
                    }
                    try {
                        Files.writeString(pathBorrowedBooks,addToBorrowed);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //ta bort från availableBooks
                    for (int i = 0; i<bookListAvailible.size()-1;i++) {
                        //ta bort matchedBook från availableBooks
                        if (matchedBook.equalsIgnoreCase(bookListAvailible.get(i))) {
                            bookListAvailible.remove(i);
                        }
                    }
                    String removedBook = "";
                    for (String item: bookListAvailible){
                        removedBook = removedBook.concat(item)+"\n";
                    }
                    try {
                        Files.writeString(pathAvailibleBooks, removedBook);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Please enter yes or no");

                }
                System.out.println(answer);
            } while (!answer.equalsIgnoreCase("no") && !answer.equalsIgnoreCase("yes"));
        }
    }

    void returnBook() {
        int matches = 0;
        String matchedBook = "";
        String choice;
        System.out.println("Which book do you want to return?");
        String bookToReturn = new java.util.Scanner(System.in).nextLine();
        for (String borrowedBook :bookListBorrowed){
            if (borrowedBook.toLowerCase().contains(bookToReturn.toLowerCase())){
                System.out.println(borrowedBook);
                matchedBook = borrowedBook;
                matches++;
            }
        }
        if (matches <1 ){
            System.out.println("No such book found, please try again");
            returnBook();
        }
        else if (matches == 1){
            do {
                System.out.print("Is this the book you want to return?");
                choice = new java.util.Scanner(System.in).nextLine();
                if (choice.equalsIgnoreCase("yes")) {
                    for (int i = 0; i< bookListBorrowed.size(); i++ )
                    if (matchedBook.equals(bookListBorrowed.get(i)))
                        bookListBorrowed.remove(i);

                    String removedBook = "";
                    for (String item: bookListBorrowed){
                        removedBook = removedBook.concat(item)+"\n";
                    }
                    try {
                        Files.writeString(pathBorrowedBooks, removedBook);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



                } else if (choice.equalsIgnoreCase("no")){
                    returnBook();
                }
                else {
                    System.out.println("Please enter yes or no.");
                }
            } while (!choice.equalsIgnoreCase("no") && !choice.equalsIgnoreCase("yes"));
        }
        else {
            System.out.println("Please be more specific, your query matched "+matches+" books");
        }
    }

    private List<String> getBooks(Path library) {

        if (Files.exists(library)) {
            System.out.println("File exists...");
            System.out.println("Printing contents...");
            try {
                //String[] wordsArray = Files.lines(path).toArray(String[]::new);
                return (ArrayList<String>) Files.lines(library).collect(Collectors.toList());
                //List <String> wordsList =  Files.lines(path).collect(Collectors.toList());


            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                Files.createFile(library);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("File created...");
        }


        try {

            return Files.readAllLines(library);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return new ArrayList<>();

    }


    private void printAllBooks(List<String> bookList) {
        for (String Books : bookList) {

            System.out.println(Books);
        }
    }

}



            

    
    
    
    
