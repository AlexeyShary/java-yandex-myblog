package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.apache.commons.io.FilenameUtils.getExtension;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public Page<PostDto> getPosts(String searchTag, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        Page<Post> page = (searchTag == null || searchTag.isBlank())
                ? postRepository.findAll(pageable)
                : postRepository.findByTagName(searchTag, pageable);

        return page.map(this::toDto);
    }

    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found: " + id));
        return toDto(post);
    }

    public ResponseEntity<Resource> getImageResponse(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        String filename = optionalPost.get().getImageUrl();

        if (filename == null || filename.isBlank()) {
            return ResponseEntity.noContent().build();
        }

        try {
            Path path = Path.of("uploads").resolve(filename);
            Resource file = new UrlResource(path.toUri());

            if (!file.exists() || !file.isReadable()) {
                return ResponseEntity.noContent().build();
            }

            String contentType = Files.probeContentType(path);
            MediaType mediaType = contentType != null
                    ? MediaType.parseMediaType(contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public void createPost(String title, String text, String tagsText, MultipartFile image) {
        Post post = Post.builder()
                .title(title)
                .text(text)
                .likesCount(0)
                .tags(new HashSet<>())
                .build();

        List<String> tagNames = parseTags(tagsText);
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
            post.getTags().add(tag);
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImageTemporarily(image);
            post.setImageUrl(imageUrl);
        }

        postRepository.save(post);
    }

    public void updatePost(Long id, String title, String text, String tagsText, MultipartFile image) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        post.setTitle(title);
        post.setText(text);

        if (image != null && !image.isEmpty()) {
            deleteOldImage(post.getImageUrl());
            String newImageUrl = saveImageTemporarily(image);
            post.setImageUrl(newImageUrl);
        }

        Set<Tag> tags = new HashSet<>();
        for (String tagName : parseTags(tagsText)) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
            tags.add(tag);
        }
        post.setTags(tags);

        postRepository.save(post);
    }

    public void likePost(Long id, boolean like) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        int delta = like ? 1 : -1;
        post.setLikesCount(post.getLikesCount() + delta);
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private PostDto toDto(Post post) {
        List<String> tagNames = post.getTags().stream()
                .map(Tag::getName)
                .toList();

        List<CommentDto> comments = Optional.ofNullable(post.getComments()).orElse(List.of()).stream()
                .map(c -> new CommentDto(c.getId(), c.getText()))
                .toList();

        String textPreview = post.getText() != null ? post.getText().split("\n")[0] : "";
        List<String> textParts = post.getText() != null
                ? Arrays.asList(post.getText().split("\\n+"))
                : List.of();

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .likesCount(post.getLikesCount())
                .textPreview(textPreview)
                .tags(tagNames)
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

    private String saveImageTemporarily(MultipartFile image) {
        try {
            String filename = "post-" + UUID.randomUUID() + "." + getExtension(image.getOriginalFilename());
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(uploadDir);

            Path path = uploadDir.resolve(filename);
            image.transferTo(path.toFile());
            return filename;
        } catch (IOException e) {
            log.error("Save image error {}", e.getMessage());
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void deleteOldImage(String imageUrl) {
        if (imageUrl != null) {
            Path path = Paths.get(System.getProperty("user.dir"), "uploads", imageUrl);
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.error("Delete old image error {}", e.getMessage());
            }
        }
    }
}
