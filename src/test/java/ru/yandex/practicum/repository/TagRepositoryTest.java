package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Tag;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagRepositoryTest {

    @InjectMocks
    private TagRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByIdWithDataTest() {
        Tag tag = new Tag();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L))).thenReturn(tag);

        Optional<Tag> result = repository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(tag, result.get());
    }

    @Test
    void findByIdWithoutDataTest() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(999L)))
                .thenThrow(EmptyResultDataAccessException.class);

        Optional<Tag> result = repository.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameWithDataTest() {
        Tag tag = new Tag();
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("tag1"))).thenReturn(tag);

        Optional<Tag> result = repository.findByName("tag1");

        assertTrue(result.isPresent());
        assertEquals(tag, result.get());
    }

    @Test
    void findByNameWithoutDataTest() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("not_found")))
                .thenThrow(EmptyResultDataAccessException.class);

        Optional<Tag> result = repository.findByName("not_found");

        assertTrue(result.isEmpty());
    }

    @Test
    void saveTest() {
        Tag tag = new Tag();
        tag.setName("new-tag");

        Tag saved = new Tag();
        saved.setId(5L);
        saved.setName("new-tag");

        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("new-tag"))).thenReturn(saved);

        Tag result = repository.save(tag);

        verify(jdbcTemplate).update("INSERT INTO tags (name) VALUES (?)", "new-tag");
        assertEquals(saved, result);
    }

    @Test
    void findAllTest() {
        List<Tag> expected = List.of(new Tag(), new Tag());

        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expected);

        List<Tag> result = repository.findAll();

        assertEquals(expected, result);
    }

    @Test
    void deleteByIdTest() {
        repository.deleteById(99L);

        verify(jdbcTemplate).update("DELETE FROM tags WHERE id = ?", 99L);
    }
}