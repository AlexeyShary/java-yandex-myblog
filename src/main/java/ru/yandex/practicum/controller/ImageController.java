package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.nio.file.Path;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final PostRepository postRepository;

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long postId) {
        Optional<Post> postOpt = postRepository.findByPostId(postId);
        if (postOpt.isEmpty() || postOpt.get().getImageUrl() == null) {
            return ResponseEntity.notFound().build();
        }

        String filename = postOpt.get().getImageUrl();
        Path path = Path.of("uploads").resolve(filename);

        try {
            Resource file = new UrlResource(path.toUri());
            if (!file.exists() || !file.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(file);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
