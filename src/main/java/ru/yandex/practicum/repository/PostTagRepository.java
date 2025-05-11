package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.PostTag;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostTagRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PostTag> rowMapper = BeanPropertyRowMapper.newInstance(PostTag.class);

    public List<PostTag> findByPostId(Long postId) {
        return jdbcTemplate.query(
                "SELECT * FROM post_tags WHERE post_id = ?",
                rowMapper,
                postId
        );
    }

    public List<PostTag> findByTagId(Long tagId) {
        return jdbcTemplate.query(
                "SELECT * FROM post_tags WHERE tag_id = ?",
                rowMapper,
                tagId
        );
    }

    public void save(PostTag postTag) {
        jdbcTemplate.update(
                "INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)",
                postTag.getPostId(),
                postTag.getTagId()
        );
    }

    public void deleteByPostId(Long postId) {
        jdbcTemplate.update("DELETE FROM post_tags WHERE post_id = ?", postId);
    }
}