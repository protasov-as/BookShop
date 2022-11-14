package com.example.MyBookShopApp.struct.book.links;

import javax.persistence.*;

@Entity
@Table(name = "book2tag")
public class Book2TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

    @Column(columnDefinition = "INT NOT NULL")
    private int tagId;
}
