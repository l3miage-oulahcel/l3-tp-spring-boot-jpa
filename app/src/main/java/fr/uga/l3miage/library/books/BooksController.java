package fr.uga.l3miage.library.books;

import fr.uga.l3miage.data.domain.Author;
import fr.uga.l3miage.data.domain.Book;
import fr.uga.l3miage.library.authors.AuthorDTO;
import fr.uga.l3miage.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // todo
    public BookDTO book(Long id) {

        return null;
    }

    //todo
    public BookDTO newBook(Long authorId, BookDTO book) {

        return null;
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
