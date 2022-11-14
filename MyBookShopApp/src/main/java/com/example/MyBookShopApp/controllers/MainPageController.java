package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.dto.GenreDto;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.dto.SearchWordDto;
import com.example.MyBookShopApp.service.GenreService;
import com.example.MyBookShopApp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final GenreService genreService;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService, GenreService genreService) {
        this.bookService = bookService;
        this.tagService = tagService;
        this.genreService = genreService;
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 6).getContent();
    }

    @ModelAttribute("genres")
    public List<GenreDto> allGenres() {
        return genreService.getAllGenresStructureDto();
    }

    @ModelAttribute("tagsMap")
    public Map<String, BigInteger> tagsPopularityMap() {
        return tagService.getMapOfTagPopularity();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/recent")
    public String mainRecentPage() {
        return "/books/recent";
    }

    @GetMapping("/popular")
    public String mainPopularPage() {
        return "/books/popular";
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "/genres/index";
    }

    @GetMapping("/tags/{tagName}")
    public String tagPage(@PathVariable("tagName") String tagName, Model model) {
        List<Book> booksByTag = bookService.getPageOfBooksByTag(tagName, 0, 5).getContent();
        model.addAttribute("booksByTag", booksByTag);
        model.addAttribute("tagName", tagName);
        return "/tags/index";
    }

    @GetMapping("/genres/{genreSlug}")
    public String genrePage(@PathVariable("genreSlug") String genreSlug, Model model) {
        List<Book> booksByGenre = bookService.getPageOfBooksByGenreSlugWithSubgenres(genreSlug, 0, 5).getContent();
        model.addAttribute("booksByGenre", booksByGenre);
        model.addAttribute("genreSlug", genreSlug);
        model.addAttribute("genreName", genreService.findGenreBySlug(genreSlug).get().getName());
        return "/genres/slug";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) {
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults",
                bookService.getPageOfSearchResultBooks(searchWordDto.getContent(), 0, 5).getContent());
        return "/search/index";
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        System.out.println("searchPage");
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getContent(), offset, limit).getContent());
    }
}
