package fr.uga.l3miage.library.books;

import fr.uga.l3miage.library.authors.AuthorDTO;
import jakarta.validation.constraints.*;

import java.util.Collection;


public record BookDTO(
        Long id,
        @NotBlank String title,
        long isbn, // 13 chiffres
        @NotBlank String publisher,
        @Min(100) @Max(2023) short year,
        String language, //
        Collection<AuthorDTO> authors
) {
}
