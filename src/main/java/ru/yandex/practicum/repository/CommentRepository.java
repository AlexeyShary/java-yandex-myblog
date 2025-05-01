package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Comment> rowMapper = BeanPropertyRowMapper.newInstance(Comment.class);

    public List<Comment> findByPostId(Long postId) {
        return jdbcTemplate.query(
                "SELECT * FROM comments WHERE post_id = ? ORDER BY created_at",
                rowMapper,
                postId
        );
    }
}