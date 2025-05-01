package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostTag;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final CommentRepository commentRepository;

    public List<PostDto> getPosts(String searchTag, int pageNumber, int pageSize) {
        int offset = pageNumber * pageSize;
        List<Post> posts;

        if (searchTag == null || searchTag.isBlank()) {
            posts = postRepository.findPaged(offset, pageSize);
        } else {
            Optional<Tag> tagOpt = tagRepository.findByName(searchTag);
            if (tagOpt.isEmpty()) return List.of();
            List<Long> postIds = postTagRepository.findByTagId(tagOpt.get().getId())
                    .stream().map(PostTag::getPostId).toList();
            posts = postRepository.findPagedByIds(postIds, offset, pageSize);
        }

        return posts.stream()
                .map(this::toDto)
                .toList();
    }

//    public int getTotalPostCount(String searchTag) {
//        if (searchTag == null || searchTag.isBlank()) {
//            return (int) postRepository.count();
//        } else {
//            Optional<Tag> tagOpt = tagRepository.findByName(searchTag);
//            if (tagOpt.isEmpty()) return 0;
//            return postTagRepository.findByTagId(tagOpt.get().getId()).size();
//        }
//    }

    private PostDto toDto(Post post) {
        List<String> tags = postTagRepository.findByPostId(post.getId()).stream()
                .map(pt -> tagRepository.findById(pt.getTagId()).orElse(null))
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .toList();

        List<CommentDto> comments = commentRepository.findByPostId(post.getId()).stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();

        String textPreview = post.getText() != null ?
                post.getText().split("\n")[0] : "";

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikesCount())
                .textPreview(textPreview)
                .tags(tags)
                .comments(comments)
                .build();
    }
}
