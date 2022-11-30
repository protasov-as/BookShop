package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.ResourceStorage;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksPagesController {

    private final BookService bookService;
    private final ResourceStorage storage;
    private final ReviewService reviewService;

    @Autowired
    public BooksPagesController(BookService bookService, ResourceStorage storage, ReviewService reviewService) {
        this.bookService = bookService;
        this.storage = storage;
        this.reviewService = reviewService;
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
        model.addAttribute("bookReviews", reviewService.getBookReviews(book.getId()));
        model.addAttribute("bookRating", reviewService.getBookRatingsMap(book.getId()));
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
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

    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash")String hash) throws IOException{
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: "+path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: "+mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: "+data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }
}
