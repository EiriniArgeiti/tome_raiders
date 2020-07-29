package com.groupproject.tomeraiders.models;

import java.util.List;

import com.groupproject.tomeraiders.models.data.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

    List<Category> findAllByOrderBySortingAsc();

}