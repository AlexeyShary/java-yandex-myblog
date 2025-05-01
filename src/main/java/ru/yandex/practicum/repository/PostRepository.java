package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);

    public List<Post> findPaged(int offset, int limit) {
        return jdbcTemplate.query(
                "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?",
                rowMapper,
                limit,
                offset
        );
    }

    public List<Post> findPagedByIds(List<Long> ids, int offset, int limit) {
        if (ids.isEmpty()) return List.of();
        String idList = ids.stream().map(String::valueOf).collect(Collectors.joining(","));
        return jdbcTemplate.query(
                "SELECT * FROM posts WHERE id IN (" + idList + ") ORDER BY created_at DESC LIMIT ? OFFSET ?",
                rowMapper,
                limit,
                offset
        );
    }
}