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

    public void save(Long postId, String text) {
        jdbcTemplate.update(
                "INSERT INTO comments (post_id, text, created_at, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                postId,
                text
        );
    }

    public void updateText(Long id, String text) {
        jdbcTemplate.update(
                "UPDATE comments SET text = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?",
                text,
                id
        );
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM comments WHERE id = ?", id);
    }
}