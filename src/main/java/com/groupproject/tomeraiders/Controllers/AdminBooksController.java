package com.groupproject.tomeraiders.Controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import com.groupproject.tomeraiders.models.BookRepository;
import com.groupproject.tomeraiders.models.CategoryRepository;
import com.groupproject.tomeraiders.models.data.Book;
import com.groupproject.tomeraiders.models.data.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/books")
public class AdminBooksController {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model) {

        List<Book> books = bookRepo.findAll();
        List<Category> categories = categoryRepo.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Category cat : categories) {
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("books", books);
        model.addAttribute("cats", cats);

        return "admin/books/index";
    }

    @GetMapping("/add")
    public String add(Book book, Model model) {

        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("categories", categories);

        return "admin/books/add";
    }

    @PostMapping("/add")
    public String add(@Valid Book book, BindingResult bindingResult, MultipartFile file,
            RedirectAttributes redirectAttributes, Model model) throws IOException {

        List<Category> categories = categoryRepo.findAll();

        if (bindingResult.hasErrors()) {

            model.addAttribute("categories", categories);
            return "admin/books/add";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Book added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = book.getTitle().toLowerCase().replace(" ", "-");

        Book bookExists = bookRepo.findBySlug(slug);

        if (!fileOK) {
            redirectAttributes.addFlashAttribute("message", "Image should be in a jpg or png format");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("book", book);

        }

        else if (bookExists != null) {
            redirectAttributes.addFlashAttribute("message", "This book already exists");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("book", book);

        } else {
            book.setSlug(slug);
            book.setImage(filename);
            bookRepo.save(book);
            Files.write(path, bytes);

        }
        return "redirect:/admin/books/add";
    }

}