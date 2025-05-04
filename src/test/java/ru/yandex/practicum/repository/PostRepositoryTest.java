package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostRepositoryTest {
    @InjectMocks
    private PostRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByPostIdWithDataTest() {
        Post post = new Post();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L))).thenReturn(post);

        Optional<Post> result = repository.findByPostId(1L);

        assertTrue(result.isPresent());
        assertEquals(post, result.get());
    }

    @Test
    void findByPostIdWithoutDataTest() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(999L)))
                .thenThrow(EmptyResultDataAccessException.class);

        Optional<Post> result = repository.findByPostId(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPagedWithDataTest() {
        List<Post> expected = List.of(new Post(), new Post());
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(10), eq(0))).thenReturn(expected);

        List<Post> result = repository.findPaged(0, 10);

        assertEquals(expected, result);
    }

    @Test
    void findPagedWithoutDataTest() {
        List<Post> result = repository.findPagedByIds(List.of(), 0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void findPagedWithDataByIdTest() {
        List<Post> expected = List.of(new Post());
        when(jdbcTemplate.query(contains("IN"), any(RowMapper.class), eq(5), eq(0))).thenReturn(expected);

        List<Post> result = repository.findPagedByIds(List.of(1L, 2L, 3L), 0, 5);

        assertEquals(expected, result);
    }

    @Test
    void saveTest() {
        Post post = new Post();
        post.setTitle("Title");
        post.setText("Text");
        post.setImageUrl("img");
        post.setLikesCount(5);

        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(42L);

        Long result = repository.save(post);

        verify(jdbcTemplate).update(
                eq("INSERT INTO posts (title, text, image_url, likes_count) VALUES (?, ?, ?, ?)"),
                eq("Title"), eq("Text"), eq("img"), eq(5)
        );
        assertEquals(42L, result);
    }

    @Test
    void updateTest() {
        Post post = new Post();
        post.setId(1L);
        post.setTitle("Updated");
        post.setText("Updated text");
        post.setImageUrl("url");

        repository.update(post);

        verify(jdbcTemplate).update(
                eq("UPDATE posts SET title = ?, text = ?, image_url = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?"),
                eq("Updated"),
                eq("Updated text"),
                eq("url"),
                eq(1L)
        );
    }

    @Test
    void updateImageUrlTest() {
        repository.updateImageUrl(7L, "newurl");

        verify(jdbcTemplate).update("UPDATE posts SET image_url = ? WHERE id = ?", "newurl", 7L);
    }

    @Test
    void updateLikesTest() {
        repository.updateLikes(5L, 1);

        verify(jdbcTemplate).update("UPDATE posts SET likes_count = likes_count + ? WHERE id = ?", 1, 5L);
    }

    @Test
    void deleteByIdTest() {
        repository.deleteById(3L);

        verify(jdbcTemplate).update("DELETE FROM posts WHERE id = ?", 3L);
    }
}