package fr.uga.l3miage.library.authors;

import fr.uga.l3miage.data.domain.Author;
import fr.uga.l3miage.library.books.BookDTO;
import fr.uga.l3miage.library.books.BooksMapper;
import fr.uga.l3miage.library.service.AuthorService;
import fr.uga.l3miage.library.service.EntityNotFoundException;

import fr.uga.l3miage.library.service.base.BaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

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

    // valided
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

    // push Lyna (valided)
   @GetMapping("/authors/{id}")
   public AuthorDTO author(@PathVariable("id") Long id) throws EntityNotFoundException {
        return null;
    }

    // valided
    @PostMapping("/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO newAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        Author newAuthor = authorMapper.dtoToEntity(authorDTO);
        Author savedAuthor = authorService.save(newAuthor);
        return authorMapper.entityToDTO(savedAuthor);
    }

    // valided
    @PutMapping("/authors/{id}")
    public AuthorDTO updateAuthor(@Valid @RequestBody AuthorDTO author, @PathVariable("id") Long id) throws EntityNotFoundException {
        // vérifie que l'identifiant dans le corps de la requête correspond à celui dans l'URL
        if (id != author.id()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // modifie l'auteur si existant
        try {
            Author updated = authorService.update(authorMapper.dtoToEntity(author));
            return authorMapper.entityToDTO(updated);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteAuthor(Long id) {
        // unimplemented... yet!
    }

    public Collection<BookDTO> books(Long authorId) {
        return Collections.emptyList();
    }

}
