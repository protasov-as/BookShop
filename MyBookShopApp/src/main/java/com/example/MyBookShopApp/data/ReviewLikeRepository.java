package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.book.review.BookReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Integer> {

    BookReviewLikeEntity findBookReviewLikeEntityByReviewIdAndUserId(int reviewId, int userId);
}
