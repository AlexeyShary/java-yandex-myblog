package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String textPreview;
    private String imageUrl;
    private int likesCount;
    private List<String> tags;
    private List<CommentDto> comments;
    private List<String> textParts;
}
