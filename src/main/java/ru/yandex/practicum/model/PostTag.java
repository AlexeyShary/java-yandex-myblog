package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("post_tags")
@NoArgsConstructor
@AllArgsConstructor
public class PostTag {
    @Id
    private transient Long id;
    private Long postId;
    private Long tagId;
}
