package com.groupproject.tomeraiders.models.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "pages")
@Data
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;

    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;
    private String slug;

    @Size(min = 10, message = "Content must be at least 10 characters long")
    private String content;
    private int sorting;

}