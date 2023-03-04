package fr.uga.l3miage.library.books;

import fr.uga.l3miage.library.authors.AuthorDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Collection;


public record BookDTO(
        Long id,
        @NotBlank String title,
        long isbn,
        @NotBlank String publisher,
        short year,
        String language,
        Collection<AuthorDTO> authors
) {
}
