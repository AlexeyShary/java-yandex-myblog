package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Tag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> rowMapper = BeanPropertyRowMapper.newInstance(Tag.class);

    public Optional<Tag> findById(Long id) {
        try {
            Tag tag = jdbcTemplate.queryForObject(
                    "SELECT * FROM tags WHERE id = ?",
                    rowMapper,
                    id
            );
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Tag> findByName(String name) {
        try {
            Tag tag = jdbcTemplate.queryForObject(
                    "SELECT * FROM tags WHERE name = ?",
                    rowMapper,
                    name
            );
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Tag save(Tag tag) {
        jdbcTemplate.update(
                "INSERT INTO tags (name) VALUES (?)",
                tag.getName()
        );

        return findByName(tag.getName()).orElseThrow();
    }

    public List<Tag> findAll() {
        return jdbcTemplate.query("SELECT * FROM tags", rowMapper);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM tags WHERE id = ?", id);
    }
}