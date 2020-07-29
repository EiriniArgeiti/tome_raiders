package com.groupproject.tomeraiders.models;

import com.groupproject.tomeraiders.models.data.Book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBySlug(String slug);

}