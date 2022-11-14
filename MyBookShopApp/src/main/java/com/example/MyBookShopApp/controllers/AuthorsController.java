package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.service.AuthorService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.struct.author.AuthorEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @ModelAttribute("authorsMap")
    public Map<String,List<Author>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @ModelAttribute("authorMap")
    public Map<String,List<AuthorEntity>> authorMap(){
        return authorService.getAuthorMap();
    }

    @GetMapping("/authors")
    public String authorsPage(){
        return "/authors/index";
    }

    @GetMapping("/authors/{authorSlug}")
    public String authorPage(@PathVariable("authorSlug") String authorSlug, Model model) {
        List<Book> booksByAuthor = bookService.getPageOfBooksByAuthorSlug(authorSlug, 0, 5).getContent();
        model.addAttribute("numberOfBooksByAuthor", bookService.countBooksByAuthorSlug(authorSlug));
        model.addAttribute("booksByAuthor", booksByAuthor);
        model.addAttribute("authorEntity", authorService.getAuthorEntityBySlug(authorSlug));
        return "/authors/slug";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String,List<Author>> authors(){
        return authorService.getAuthorsMap();
    }
}
