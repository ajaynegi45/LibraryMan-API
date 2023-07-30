package com.ajaynegi.SpringBoot.RESTBookAPI.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String bookName;
    private String authorName;


    public BookEntity() {
        System.out.println("Empty Constructor EntitiesBook");
    }

    public BookEntity(int id, String bookName, String authorName) {
        System.out.println("All Constructor EntitiesBook");
        this.id = id;
        this.bookName = bookName;
        this.authorName = authorName;
    }

    public int getId() {
        System.out.println("\n Getting Id EntitiesBook...");
        return id;
    }

    public void setId(int id) {
        System.out.println("\n Setting Id EntitiesBook...");
        this.id = id;
    }

    public String getBookName() {
        System.out.println("\n Getting Book Name EntitiesBook...");
        return bookName;
    }

    public void setBookName(String bookName) {
        System.out.println("\n Setting Book Name EntitiesBook...");
        this.bookName = bookName;
    }

    public String getAuthorName() {
        System.out.println("\n Getting Author Name EntitiesBook...");
        return authorName;
    }

    public void setAuthorName(String authorName) {
        System.out.println("\n Setting Author Name EntitiesBook...");
        this.authorName = authorName;
    }
}
