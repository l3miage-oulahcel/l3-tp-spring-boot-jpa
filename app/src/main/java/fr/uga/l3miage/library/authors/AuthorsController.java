package fr.uga.l3miage.library.authors;

import fr.uga.l3miage.data.domain.Author;
import fr.uga.l3miage.library.books.BookDTO;
import fr.uga.l3miage.library.books.BooksMapper;
import fr.uga.l3miage.library.service.AuthorService;
import fr.uga.l3miage.library.service.EntityNotFoundException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
public class AuthorsController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final BooksMapper booksMapper;

    @Autowired
    public AuthorsController(AuthorService authorService, AuthorMapper authorMapper, BooksMapper booksMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
        this.booksMapper = booksMapper;
    }

    @GetMapping("/authors")
    public Collection<AuthorDTO> authors(@RequestParam(value = "q", required = false) String query) {
        Collection<Author> authors;
        if (query == null) { // si aucun paramètre de requête (nom)
            authors = authorService.list();
        } else { // recherche l'auteur selon son nom
            authors = authorService.searchByName(query);
        }
        return authors.stream() // transforme la collection en un flux de données
                .map(authorMapper::entityToDTO) // crée un tableau d'entityToDTO
                .toList(); // collecte tous les éléments du flux dans une nouvelle liste
    }

   @GetMapping("/authors/{id}")
    public AuthorDTO author(Long id) throws EntityNotFoundException {
        // ...
       return null;
    }

    // fonctionnelle (manque le fait de gérer un doublon)
    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO newAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        Author newAuthor = authorMapper.dtoToEntity(authorDTO);
        Author savedAuthor = authorService.save(newAuthor);
        return authorMapper.entityToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(AuthorDTO author, Long id) {
        // attention AuthorDTO.id() doit être égale à id, sinon la requête utilisateur est mauvaise
        return null;
    }

    public void deleteAuthor(Long id) {
        // unimplemented... yet!
    }

    public Collection<BookDTO> books(Long authorId) {
        return Collections.emptyList();
    }

}
