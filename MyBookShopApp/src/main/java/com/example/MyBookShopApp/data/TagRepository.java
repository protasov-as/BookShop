package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    @Query(value = "select y.tag, y.sum " +
            "FROM (select t.tag as tag, count(b.book_id) as sum " +
            "from tags t " +
            "join book2tag b " +
            "on t.id = b.tag_id " +
            "group by t.tag) y",
            nativeQuery = true)
    public List<Object[]> getAllTagsCount();

    @Query(value = "select MAX(y.sum) " +
            "FROM (select count(b.book_id) as sum " +
            "from tags t " +
            "join book2tag b " +
            "on t.id = b.tag_id " +
            "group by t.tag " +
            "order by sum desc) y",
            nativeQuery = true)
    public int getMaxTagCount();

    @Query(value = "select MIN(y.sum) " +
            "FROM (select count(b.book_id) as sum " +
            "from tags t " +
            "join book2tag b " +
            "on t.id = b.tag_id " +
            "group by t.tag " +
            "order by sum desc) y",
            nativeQuery = true)
    public int getMinTagCount();
}
