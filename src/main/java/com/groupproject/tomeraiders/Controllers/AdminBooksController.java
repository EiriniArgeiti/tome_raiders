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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;

        Pageable pageable = PageRequest.of(page, perPage);

        Page<Book> books = bookRepo.findAll(pageable);
        List<Category> categories = categoryRepo.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Category cat : categories) {
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("books", books);
        model.addAttribute("cats", cats);

        long count = bookRepo.count();
        double pageCount = Math.ceil((double) count / (double) perPage);

        model.addAttribute("pageCount", (int) pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Book book = bookRepo.getOne(id);
        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("book", book);
        model.addAttribute("categories", categories);

        return "admin/books/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Book book, BindingResult bindingResult, MultipartFile file,
            RedirectAttributes redirectAttributes, Model model) throws IOException {

        Book currentBook = bookRepo.getOne(book.getId());

        List<Category> categories = categoryRepo.findAll();

        if (bindingResult.hasErrors()) {

            model.addAttribute("bookTitle", currentBook.getTitle());
            model.addAttribute("categories", categories);
            return "admin/books/edit";
        }

        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (!file.isEmpty()) {
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Book edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = book.getTitle().toLowerCase().replace(" ", "-");

        Book bookExists = bookRepo.findBySlugAndIdNot(slug, book.getId());

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

            if (!file.isEmpty()) {
                book.setImage(filename);
                Files.write(path, bytes);
            } else {
                book.setImage(currentBook.getImage());
            }

            bookRepo.save(book);

        }
        return "redirect:/admin/books/edit/" + book.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

        bookRepo.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Book deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/books";

    }

}