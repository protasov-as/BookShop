package com.example.MyBookShopApp.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findBooksByAuthor_FirstName(String name);

    @Query("from Book")
    List<Book> customFindAllBooks();

    //NEW BOOK REST REPOSITORY COMMANDS

    List<Book> findBooksByAuthorFirstNameContaining(String authorFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    Book findBookBySlug(String slug);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value =
            "SELECT * from books " +
                    "JOIN (SELECT book_id, " +
                    "SUM (CASE " +
                    "WHEN type_id = 3 THEN 1 " +
                    "WHEN type_id = 2 THEN 0.7 " +
                    "ELSE 0.4 END) " +
                    "as rating FROM public.book2user " +
                    "GROUP BY book_id) book2user " +
                    "ON books.id = book2user.book_id " +
                    "ORDER BY rating DESC",
            nativeQuery = true)
    Page<Book> getPopularBooks(Pageable nextPage);

    @Query(value =
            "select * " +
                    "from books b " +
                    "join book2tag bt " +
                    "on b.id = bt.book_id " +
                    "join tags t " +
                    "on bt.tag_id = t.id " +
                    "where t.tag = :tag"
            , nativeQuery = true)
    Page<Book> getBooksByTag(String tag, Pageable nextPage);

    @Query(value ="select * " +
            "from books b " +
            "join book2genre b2g " +
            "on b.id = b2g.book_id " +
            "join genre g " +
            "on b2g.genre_id = g.id " +
            "where g.slug = :slug"
            , nativeQuery = true)
    Page<Book> getBooksByGenreSlug(String slug, Pageable nextPage);

    @Query(value ="select * " +
            "from books b " +
            "join book2genre b2g " +
            "on b.id = b2g.book_id " +
            "join genre g " +
            "on b2g.genre_id = g.id " +
            "where g.slug = :slug"
            , nativeQuery = true)
    List<Book> getBooksListByGenreSlug(String slug);

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    @Query(value = "SELECT * FROM books ORDER BY pub_date DESC", nativeQuery = true)
    Page<Book> getBooksSortedByPubDate(Pageable nextPage);

    Page<Book> findBooksByTitleContainingIgnoreCase(String bookTitle, Pageable nextPage);

    Page<Book> findBooksByPubDateBetween(Date from, Date to, Pageable nextPage);

    @Query(value ="select count(*) " +
            "from books b " +
            "join book2author b2a " +
            "on b.id = b2a.book_id " +
            "join author a " +
            "on b2a.author_id = a.id " +
            "where a.slug = :authorSlug"
            , nativeQuery = true)
    Integer countBooksByAuthorSlug(String authorSlug);

    @Query(value ="select * " +
            "from books b " +
            "join book2author b2a " +
            "on b.id = b2a.book_id " +
            "join author a " +
            "on b2a.author_id = a.id " +
            "where a.slug = :authorSlug"
            , nativeQuery = true)
    Page<Book> getBooksByAuthorSlug(String authorSlug, Pageable nextPage);

    List<Book> findBooksBySlugIn(String[] slugs);
}
