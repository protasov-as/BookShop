package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {

    List<GenreEntity> findByParentIdIsNull();

    List<GenreEntity> findByParentIdIs(Integer parentId);

    Optional<GenreEntity> findBySlug(String slug);

    @Query(value ="select count(*) " +
            "from book2genre " +
            "where genre_id = :id"
            , nativeQuery = true)
    Integer countBooksByGenreId(int id);
}
