package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.struct.genre.GenreEntity;
import io.swagger.v3.oas.models.headers.Header;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookService {

    private BookRepository bookRepository;
    private GenreService genreService;

    @Autowired
    public BookService(BookRepository bookRepository, GenreService genreService) {
        this.bookRepository = bookRepository;
        this.genreService = genreService;
    }

    public List<Book> getBooksData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SERVICE METHODS

    public List<Book> getBooksByAuthor(String authorName){
        return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title){
        return bookRepository.findBooksByTitleContaining(title);
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max){
        return bookRepository.findBooksByPriceOldBetween(min,max);
    }

    public List<Book> getBooksWithPrice(Integer price){
        return bookRepository.findBooksByPriceOldIs(price);
    }

    public List<Book> getBooksWithMaxPrice(){
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers(){
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfPopularBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.getPopularBooks(nextPage);
    }

    public Page<Book> getPageOfBooksByTag(String tag, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.getBooksByTag(tag, nextPage);
    }

    public Page<Book> getPageOfBooksByGenreSlug(String slug, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.getBooksByGenreSlug(slug, nextPage);
    }

    public Page<Book> getPageOfBooksByGenreSlugWithSubgenres(String slug, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        List<Book> bookList = new ArrayList<>();
        List<GenreEntity> genreList = genreService.findGenreWithSubgenresBySlug(slug);
        genreList.forEach(g -> bookList.addAll(bookRepository.getBooksListByGenreSlug(g.getSlug())));
        int start = Math.min((int) nextPage.getOffset(), bookList.size());
        int end = (int) (Math.min((start + nextPage.getPageSize()), bookList.size()));
        return new PageImpl<Book>(bookList.subList(start, end), nextPage, bookList.size());
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.findBooksByTitleContainingIgnoreCase(searchWord,nextPage);
    }

    public Page<Book> getPageOfRecentBooks(Date from, Date to, Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset, limit);
        if(from == null && to == null) {
            return bookRepository.getBooksSortedByPubDate(nextPage);
        }
        else {
            nextPage = PageRequest.of(offset, limit, Sort.by("pubDate").descending());
            return bookRepository.findBooksByPubDateBetween(from, to, nextPage);
        }
    }

    public Page<Book> getPageOfRecentBooks(Integer offset, Integer limit){
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.getBooksSortedByPubDate(nextPage);
    }

    public Integer countBooksByAuthorSlug(String authorSlug){
        return bookRepository.countBooksByAuthorSlug(authorSlug);
    }
    public Page<Book> getPageOfBooksByAuthorSlug(String AuthorSlug, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset,limit);
        return bookRepository.getBooksByAuthorSlug(AuthorSlug, nextPage);
    }

    public Book findBookBySlug(String slug) {
        return bookRepository.findBookBySlug(slug);
    }

    public void save(Book bookToUpdate) {
        bookRepository.save(bookToUpdate);
    }
}
