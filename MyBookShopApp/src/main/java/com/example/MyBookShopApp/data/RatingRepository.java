package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.book.rating.BookRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<BookRatingEntity, Integer> {

    List<BookRatingEntity> findByBookId(Integer bookId);
    BookRatingEntity findBookRatingEntityByBookIdAndUserId(int bookId, int userId);

    Integer countAllByBookId(int bookId);
}
