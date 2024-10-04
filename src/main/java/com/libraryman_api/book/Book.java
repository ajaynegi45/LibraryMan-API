package com.libraryman_api.book;

import jakarta.persistence.*;
import lombok.*;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Book {
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "book_id_generator")
    @SequenceGenerator(name = "book_id_generator",
            sequenceName = "book_id_sequence",
            allocationSize = 1)
    @Column(name = "book_id")
    private int bookId;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String publisher;

    @Column(name = "published_year")
    private int publishedYear;
    private String genre;


    @Column(name = "copies_available", nullable = false)
    private int copiesAvailable;


}
