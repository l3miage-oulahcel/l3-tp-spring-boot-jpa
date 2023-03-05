package fr.uga.l3miage.library.books;

import fr.uga.l3miage.data.domain.Author;
import fr.uga.l3miage.data.domain.Book;
import fr.uga.l3miage.library.authors.AuthorDTO;
import fr.uga.l3miage.library.service.AuthorService;
import fr.uga.l3miage.library.service.BookService;
import fr.uga.l3miage.library.service.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    // valided
    @GetMapping("/books/{id}")
    public BookDTO book(@PathVariable("id") Long id) throws EntityNotFoundException {
        try {
            Book book = bookService.get(id);
            return booksMapper.entityToDTO(book);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // Ajout de cette méthode pour avoir tous les livres d'un auteur
    @GetMapping("/authors/{id}/books")
    public Collection<BookDTO> booksAuthor(@PathVariable("id") Long id, @RequestParam(value = "q", required = false) String query) throws EntityNotFoundException {
        Collection<Book> books;
        if (query == null) { // si aucun paramètre de requête (titre)
            try {
                books = bookService.getByAuthor(id);
                return books.stream() // transforme la collection en un flux de données
                        .map(booksMapper::entityToDTO) // crée un tableau d'entityToDTO
                        .toList(); // collecte tous les éléments du flux dans une nouvelle liste
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } else { // recherche avec un filtre
            try {
                books = bookService.findByAuthor(id, query);
                return books.stream() // transforme la collection en un flux de données
                        .map(booksMapper::entityToDTO) // crée un tableau d'entityToDTO
                        .toList(); // collecte tous les éléments du flux dans une nouvelle liste
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
    }

    // valided
    @PostMapping("/authors/{id}/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO newBook(@PathVariable("id") Long authorId, @Valid @RequestBody BookDTO book) throws EntityNotFoundException {
        try {
            Book newBook = booksMapper.dtoToEntity(book);
            Book saved = bookService.save(authorId, newBook);
            return booksMapper.entityToDTO(saved);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // probleme : auteur null
    @PutMapping("/books/{id}")
    public BookDTO updateBook(@PathVariable("id") Long authorId, @RequestBody BookDTO book) throws EntityNotFoundException {
        // modifie le livre si existant
        try {
            Book updated = bookService.update(booksMapper.dtoToEntity(book));
            return booksMapper.entityToDTO(updated);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // valided
    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) throws EntityNotFoundException {
        try {
            bookService.delete(id);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    // valided (mais bug dans test)
    @PutMapping("/books/{id}/authors")
    public void addAuthor(@PathVariable("id") Long bookId, @Valid @RequestBody AuthorDTO authorDTO) throws EntityNotFoundException{
        try {
            Book updated = bookService.addAuthor(bookId, authorDTO.id());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
