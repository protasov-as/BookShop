package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.dto.BooksPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/books")
public class BooksPagesController {

    private final BookService bookService;
    private final ResourceStorage storage;

    @Autowired
    public BooksPagesController(BookService bookService, ResourceStorage storage) {
        this.bookService = bookService;
        this.storage = storage;
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/recent")
    @ResponseBody
    public BooksPageDto getBooksRecentPage(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
                                           @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        if(from != null && to != null) {
            return new BooksPageDto(
                    bookService.getPageOfRecentBooks(
                            new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), offset, limit
                    ).getContent());
        }
        else {
            return new BooksPageDto(
                    bookService.getPageOfRecentBooks(offset, limit).getContent());
        }
    }

    @GetMapping("/tag/{tagName}")
    public BooksPageDto getTagPage(@RequestParam(value = "offset", required = false) Integer offset,
                          @RequestParam(value = "limit", required = false) Integer limit,
                          @PathVariable("tagName") String tagName, Model model) {
        return new BooksPageDto(bookService.getPageOfBooksByTag(tagName, offset, limit).getContent());
    }

    @GetMapping("/genre/{genreName}")
    public BooksPageDto getGenrePage(@RequestParam(value = "offset", required = false) Integer offset,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   @PathVariable("genreName") String genreName, Model model) {
        return new BooksPageDto(bookService.getPageOfBooksByGenreSlugWithSubgenres(genreName, offset, limit).getContent());
    }

    @GetMapping("/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model){
        Book book = bookService.findBookBySlug(slug);
        model.addAttribute("slugBook", book);
        return "books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug")String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file,slug);
        System.out.println(slug);
        Book bookToUpdate = bookService.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookService.save(bookToUpdate); //save new path in db here
        return "redirect:/books/"+slug;
    }
}
