package com.libraryman_api.book;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BookDto {


    private int bookId;

    private String title;

    private String author;

    private String isbn;

    private String publisher;

    private int publishedYear;

    private String genre;


    private int copiesAvailable;



}
