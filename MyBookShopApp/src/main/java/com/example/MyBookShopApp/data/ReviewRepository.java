package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<BookReviewEntity, Integer> {

    List<BookReviewEntity> findByBookId(Integer bookId);

    BookReviewEntity findBookReviewEntityByBookIdAndUserId(int bookId, int userId);
}
