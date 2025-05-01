package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.PostDto;
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
    public String getPosts(
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
    public String getPostById(@PathVariable int postId) {
        return TemplateNames.POST.name;
    }

    @GetMapping("/add")
    public String getPostAddForm() {
        return TemplateNames.ADD_POST.name;
    }
}
