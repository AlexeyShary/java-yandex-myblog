package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.util.TemplateNames;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public String addComment(
            @PathVariable("postId") Long postId,
            @RequestParam("text") String text
    ) {
        commentService.addComment(postId, text);
        return TemplateNames.REDIRECT_POSTS.name + "/" + postId;
    }

    @PostMapping("/{commentId}")
    public String editComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("text") String text
    ) {
        commentService.updateComment(commentId, text);
        return TemplateNames.REDIRECT_POSTS.name + "/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(commentId);
        return TemplateNames.REDIRECT_POSTS.name + "/" + postId;
    }
}
