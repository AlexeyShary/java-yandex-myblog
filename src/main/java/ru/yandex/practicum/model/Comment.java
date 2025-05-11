package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@Table("comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private Long id;

    private Long postId;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}