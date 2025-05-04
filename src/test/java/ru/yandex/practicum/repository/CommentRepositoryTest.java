package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Comment;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentRepositoryTest {
    @InjectMocks
    private CommentRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByPostIdTest() {
        long postId = 1L;
        List<Comment> expected = List.of(new Comment(), new Comment());
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(postId))).thenReturn(expected);

        List<Comment> actual = repository.findByPostId(postId);

        assertEquals(expected, actual);
    }

    @Test
    void saveTest() {
        repository.save(1L, "hello");

        verify(jdbcTemplate).update(
                eq("INSERT INTO comments (post_id, text, created_at, updated_at) VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)"),
                eq(1L),
                eq("hello")
        );
    }

    @Test
    void updateTextTest() {
        repository.updateText(42L, "updated text");

        verify(jdbcTemplate).update(
                eq("UPDATE comments SET text = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?"),
                eq("updated text"),
                eq(42L)
        );
    }

    @Test
    void deleteByIdTest() {
        repository.deleteById(99L);

        verify(jdbcTemplate).update("DELETE FROM comments WHERE id = ?", 99L);
    }
}