package com.example.lab03.service;

import com.example.lab03.domain.Library;
import com.example.lab03.repository.BookRepository;
import com.example.lab03.repository.LibraryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookRepository bookRepository;

    public LibraryService(LibraryRepository libraryRepository, BookRepository bookRepository) {
        this.libraryRepository = libraryRepository;
        this.bookRepository = bookRepository;
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Library not found with id " + id
                ));
    }

    public Library createLibrary(Library library) {
        return libraryRepository.save(library);
    }

    @Transactional
    public void deleteLibrary(Long id) {
        if (!libraryRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Library not found with id " + id
            );
        }

        long bookCount = bookRepository.countByLibraryId(id);
        if (bookCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete library " + id + " because it still has " + bookCount + " book(s)."
            );
        }

        libraryRepository.deleteById(id);
    }
}
