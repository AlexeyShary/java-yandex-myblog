package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void addComment(Long postId, String text) {
        Comment comment = Comment.builder()
                .text(text)
                .post(getPost(postId))
                .build();

        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, String text) {
        Comment comment = getComment(commentId);

        comment.setText(text);
        comment.setUpdatedAt(LocalDateTime.now());

        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId).get();
    }
}
