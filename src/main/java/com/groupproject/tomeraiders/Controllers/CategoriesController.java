package com.groupproject.tomeraiders.Controllers;

import java.util.HashMap;
import java.util.List;

import com.groupproject.tomeraiders.models.BookRepository;
import com.groupproject.tomeraiders.models.CategoryRepository;
import com.groupproject.tomeraiders.models.data.Book;
import com.groupproject.tomeraiders.models.data.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private BookRepository bookRepo;

    @GetMapping("/{slug}")
    public String index(@PathVariable String slug, Model model,
            @RequestParam(value = "page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);
        long count = 0;

        if (slug.equals("all")) {

            Page<Book> books = bookRepo.findAll(pageable);
            count = bookRepo.count();
            model.addAttribute("books", books);

        } else {
            Category category = categoryRepo.findBySlug(slug);

            if (category == null) {
                return "redirect:/";
            }
            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Book> books = bookRepo.findAllByCategoryId(Integer.toString(categoryId), pageable);

            count = bookRepo.countByCategoryId(Integer.toString(categoryId));

            model.addAttribute("books", books);
            model.addAttribute("categoryName", categoryName);
        }
        model.addAttribute("count", count);

        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "books";
    }
}