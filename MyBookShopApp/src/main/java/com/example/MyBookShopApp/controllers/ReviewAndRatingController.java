package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookRatingValue;
import com.example.MyBookShopApp.dto.BookReviewRatingValue;
import com.example.MyBookShopApp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewAndRatingController {

    private ReviewService reviewService;

    @Autowired
    public ReviewAndRatingController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/book/rateBookReview/{bookSlug}")
    public String handleBookReviewRateChange(@RequestBody BookReviewRatingValue reviewRateValue,
                                               @PathVariable("bookSlug") String bookSlug) {
        reviewService.saveReviewRate(reviewRateValue.getReviewid(), reviewRateValue.getValue());
        return "redirect:/books/" + bookSlug;
    }

    @PostMapping("/book/{slug}/review/save")
    public String saveNewBookReview(@RequestParam String comment, @RequestParam(required = false) String authorName,
                                    @PathVariable("slug") String slug) {
        reviewService.saveReview(slug, comment, authorName);
        return "redirect:/books/" + slug;
    }

    @PostMapping("/book/changeBookStatus/rating/{slug}")
    public String addRatingValue(@RequestBody BookRatingValue bookRatingValue, @PathVariable("slug") String slug) {
        reviewService.addRating(slug, bookRatingValue.getValue());
        return "redirect:/books/" + slug;
    }
}
