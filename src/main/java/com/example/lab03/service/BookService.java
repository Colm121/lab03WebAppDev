package com.example.lab03.service;

import com.example.lab03.domain.Book;
import com.example.lab03.domain.Library;

import com.example.lab03.repository.BookRepository;
import com.example.lab03.repository.LibraryRepository;
import org.springframework.stereotype.Service;


import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    public BookService(BookRepository bookRepository, LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByLibrary(Long libraryId) {
        if (!libraryRepository.existsById(libraryId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Library not found with id " + libraryId
            );
        }
        return bookRepository.findByLibraryId(libraryId);
    }

    public Book createBook(Long libraryId, Book book) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Library not found with id " + libraryId
                ));

        book.setLibrary(library);
        return bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found with id " + bookId
            );
        }
        bookRepository.deleteById(bookId);
    }
}