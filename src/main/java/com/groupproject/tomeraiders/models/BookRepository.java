package com.groupproject.tomeraiders.models;

import java.util.List;

import com.groupproject.tomeraiders.models.data.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBySlug(String slug);

    Book findBySlugAndIdNot(String slug, int id);

    Page<Book> findAll(Pageable pageable);

    List<Book> findAllByCategoryId(String categoryId, Pageable pageable);

    long countByCategoryId(String categoryId);

}