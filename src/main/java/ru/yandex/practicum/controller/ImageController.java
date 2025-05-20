package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.service.PostService;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final PostService postService;

    @GetMapping("/images/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Long postId) {
        return postService.getImageResponse(postId);
    }
}
