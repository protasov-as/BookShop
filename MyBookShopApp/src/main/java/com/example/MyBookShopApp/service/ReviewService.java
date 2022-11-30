package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.security.BookstoreUser;
import com.example.MyBookShopApp.security.BookstoreUserDetailsService;
import com.example.MyBookShopApp.struct.book.rating.BookRatingEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewLikeRepository reviewLikeRepository;
    private BookRepository bookRepository;
    private RatingRepository ratingRepository;
    private BookstoreUserDetailsService bookstoreUserDetailsService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReviewLikeRepository reviewLikeRepository, BookRepository bookRepository, RatingRepository ratingRepository, BookstoreUserDetailsService bookstoreUserDetailsService) {
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
        this.bookRepository = bookRepository;
        this.ratingRepository = ratingRepository;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    }

    public List<BookReviewEntity> getBookReviews(Integer bookId) {
        List<BookReviewEntity> reviewsList = reviewRepository.findByBookId(bookId);
        Collections.sort(reviewsList, new Comparator<BookReviewEntity>() {
            @Override
            public int compare(BookReviewEntity o1, BookReviewEntity o2) {
                return Double.compare(o2.countRating(), o1.countRating());
            }
        });
        return reviewsList;
    }

    public List<BookRatingEntity> getBookRatings(Integer bookId) {
        return ratingRepository.findByBookId(bookId);
    }

    public Map<Integer, Long> getBookRatingsMap(Integer bookId) {
        List<BookRatingEntity> tempLst = ratingRepository.findByBookId(bookId);
        Map<Integer, Long> bookRatingsMap = tempLst.stream().collect(Collectors.groupingBy(BookRatingEntity::getRating, Collectors.counting()));

        AtomicLong sumRatings = new AtomicLong();
        bookRatingsMap.forEach((k,v) -> {
            sumRatings.addAndGet(k * v);
        });
        bookRatingsMap.put(6, bookRatingsMap.values().stream().reduce(0L, Long::sum));
        bookRatingsMap.put(7, sumRatings.get() / bookRatingsMap.get(6));
//        System.out.println(bookRatingsMap);
        return bookRatingsMap;
    }

    public void saveReviewRate(Integer reviewId, short value) {
        BookReviewEntity bookReviewEntity = reviewRepository.findById(reviewId).get();
        BookReviewLikeEntity reviewLikeEntity = reviewLikeRepository.findBookReviewLikeEntityByReviewIdAndUserId(reviewId, getCurrentUserId());
        if(reviewLikeEntity == null) {
            reviewLikeEntity = new BookReviewLikeEntity();
            reviewLikeEntity.setUserId(getCurrentUserId());
            reviewLikeEntity.setReview(bookReviewEntity);
        }
        reviewLikeEntity.setValue(value);
        reviewLikeEntity.setTime(LocalDateTime.now());
        reviewLikeRepository.save(reviewLikeEntity);
    }

    public void saveReview(String slug, String comment, String authorName) {
        Book book = bookRepository.findBookBySlug(slug);
        BookReviewEntity reviewEntity = reviewRepository.findBookReviewEntityByBookIdAndUserId(book.getId(), getCurrentUserId());
        if(reviewEntity == null) {
            reviewEntity = new BookReviewEntity();
            reviewEntity.setBookId(book.getId());
            reviewEntity.setUser(getCurrentUser());
        }
            reviewEntity.setText(comment);
            reviewEntity.setTime(LocalDateTime.now());
            reviewRepository.save(reviewEntity);
    }

    public void addRating(String slug, Integer value) {
        Book book = bookRepository.findBookBySlug(slug);
        BookRatingEntity ratingEntity = ratingRepository.findBookRatingEntityByBookIdAndUserId(book.getId(), getCurrentUserId());
        if(ratingEntity == null) {
            ratingEntity = new BookRatingEntity();
            ratingEntity.setBookId(book.getId());
            ratingEntity.setUserId(getCurrentUserId());
        }
        ratingEntity.setRating(value);
        ratingEntity.setTime(LocalDateTime.now());
        ratingRepository.save(ratingEntity);
    }

    private Integer getCurrentUserId(){
        System.out.println(bookstoreUserDetailsService.getCurrentUser().getId());
        return bookstoreUserDetailsService.getCurrentUser().getId();
    }

    private BookstoreUser getCurrentUser(){
        System.out.println(bookstoreUserDetailsService.getCurrentUser().getId());
        return bookstoreUserDetailsService.getCurrentUser();
    }
}
