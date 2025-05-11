package ru.yandex.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findPaged(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id IN :ids ORDER BY p.createdAt DESC")
    List<Post> findPagedByIds(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE LOWER(t.name) = LOWER(:tagName)")
    Page<Post> findByTagName(@Param("tagName") String tagName, Pageable pageable);
}