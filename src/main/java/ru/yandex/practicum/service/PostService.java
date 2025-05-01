package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostTag;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.PostTagRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.Arrays;
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

    public PostDto getPostById(long id) {
        Post post = postRepository.findByPostId(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        return toDto(post);
    }

    public void createPost(String title, String text, String tagsText, MultipartFile image) {
        String imageUrl = null;

        Post post = Post.builder()
                .title(title)
                .text(text)
                .imageUrl(imageUrl)
                .likesCount(0)
                .build();

        Long postId = postRepository.save(post);

        List<String> tags = parseTags(tagsText);
        saveTags(tags, postId);
    }

    public void updatePost(Long id, String title, String text, String tagsText, MultipartFile image) {
        String imageUrl = null;

        Post post = Post.builder()
                .id(id)
                .title(title)
                .text(text)
                .imageUrl(imageUrl)
                .build();

        postRepository.update(post);
        postTagRepository.deleteByPostId(id);

        List<String> tags = parseTags(tagsText);
        saveTags(tags, id);
    }

    public void likePost(Long id, boolean like) {
        postRepository.updateLikes(id, like ? 1 : -1);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

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

        List<String> textParts = Arrays.asList(post.getText().split("\\n+"));

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikesCount())
                .textPreview(textPreview)
                .tags(tags)
                .comments(comments)
                .textParts(textParts)
                .build();
    }

    private List<String> parseTags(String tagsText) {
        return Arrays.stream(tagsText.split("[,\\s]+"))
                .map(String::trim)
                .filter(t -> !t.isBlank())
                .map(String::toLowerCase)
                .distinct()
                .toList();
    }

    private void saveTags(List<String> tags, long postId) {
        for (String tagName : tags) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(null, tagName)));

            postTagRepository.save(new PostTag(null, postId, tag.getId()));
        }
    }
}
