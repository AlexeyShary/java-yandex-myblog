package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Table("posts")
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private Long id;

    private String title;
    private String text;
    private String imageUrl;
    private int likesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @MappedCollection(idColumn = "post_id")
    private List<Comment> comments;
}