package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.PostTag;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostTagRepositoryTest {
    @InjectMocks
    private PostTagRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByPostIdTest() {
        long postId = 1L;
        List<PostTag> expected = List.of(new PostTag(), new PostTag());

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(postId))).thenReturn(expected);

        List<PostTag> actual = repository.findByPostId(postId);

        assertEquals(expected, actual);
    }

    @Test
    void findByTagIdTest() {
        long tagId = 2L;
        List<PostTag> expected = List.of(new PostTag());

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(tagId))).thenReturn(expected);

        List<PostTag> actual = repository.findByTagId(tagId);

        assertEquals(expected, actual);
    }

    @Test
    void saveTest() {
        PostTag tag = new PostTag();
        tag.setPostId(10L);
        tag.setTagId(20L);

        repository.save(tag);

        verify(jdbcTemplate).update(
                eq("INSERT INTO post_tags (post_id, tag_id) VALUES (?, ?)"),
                eq(10L),
                eq(20L)
        );
    }

    @Test
    void deleteByPostIdTest() {
        repository.deleteByPostId(7L);

        verify(jdbcTemplate).update("DELETE FROM post_tags WHERE post_id = ?", 7L);
    }
}