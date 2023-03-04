package fr.uga.l3miage.library.books;

import fr.uga.l3miage.data.domain.Author;
import fr.uga.l3miage.data.domain.Book;
import fr.uga.l3miage.library.authors.AuthorDTO;
import fr.uga.l3miage.library.service.BookService;
import fr.uga.l3miage.library.service.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class BooksController {

    private final BookService bookService;
    private final BooksMapper booksMapper;

    @Autowired
    public BooksController(BookService bookService, BooksMapper booksMapper) {
       this.bookService = bookService;
        this.booksMapper = booksMapper;
    }

    // valided
    @GetMapping("/books")
    public Collection<BookDTO> books(@RequestParam(value = "q", required = false) String query) {
        Collection<Book> books;
        if (query == null) { // si aucun paramètre de requête (titre)
            books = bookService.list();
        } else { // recherche avec un filtre
            books = bookService.findByTitle(query);
        }
        return books.stream() // transforme la collection en un flux de données
                .map(booksMapper::entityToDTO) // crée un tableau d'entityToDTO
                .toList(); // collecte tous les éléments du flux dans une nouvelle liste
    }

    // à valider
    @GetMapping("/books/{id}")
    public BookDTO book(@PathVariable("id") Long id) throws EntityNotFoundException {
        try {
            Book book = bookService.get(id);
            return booksMapper.entityToDTO(book);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/authors/{id}/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO newBook(@PathVariable("id") Long authorId, @Valid @RequestBody BookDTO book) {
        try {
            Book newBook = booksMapper.dtoToEntity(book);
            Book saved = bookService.save(authorId, newBook);
            return booksMapper.entityToDTO(saved);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // todo
    public BookDTO updateBook(Long authorId, BookDTO book) {
        // attention BookDTO.id() doit être égale à id, sinon la requête utilisateur est mauvaise
        return null;
    }

    // todo
    public void deleteBook(Long id) {

    }

    // todo
    public void addAuthor(Long authorId, AuthorDTO author) {

    }
}
