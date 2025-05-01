package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.PostEditDto;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.dto.PagingDto;
import ru.yandex.practicum.util.TemplateNames;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public String getPostsForm(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Model model
    ) {
        List<PostDto> posts = postService.getPosts(search, pageNumber, pageSize);
        model.addAttribute("posts", posts);
        model.addAttribute("paging", new PagingDto(pageNumber, pageSize, posts.size()));
        model.addAttribute("search", search);

        return TemplateNames.POSTS.name;
    }

    @GetMapping("/{postId}")
    public String getPostByIdForm(@PathVariable("postId") Long postId, Model model) {
        PostDto post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return TemplateNames.POST.name;
    }

    @GetMapping("/add")
    public String getPostAddForm(Model model) {
        model.addAttribute("post", null);
        return TemplateNames.EDIT.name;
    }

    @GetMapping("/{id}/edit")
    public String getPostEditForm(@PathVariable("id") Long id, Model model) {
        PostDto post = postService.getPostById(id);

        model.addAttribute("post", PostEditDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getTextPreview())
                .tagsAsText(String.join(",", post.getTags()))
                .build());

        return TemplateNames.EDIT.name;
    }

    @PostMapping
    public String createPost(
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("tags") String tags,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        postService.createPost(title, text, tags, image);
        return TemplateNames.REDIRECT_POSTS.name;
    }

    @PostMapping("/{id}")
    public String updatePost(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam("tags") String tags,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        postService.updatePost(id, title, text, tags, image);
        return TemplateNames.REDIRECT_POSTS.name;
    }
}
