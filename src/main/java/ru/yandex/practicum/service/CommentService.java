package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void addComment(Long postId, String text) {
        commentRepository.save(postId, text);
    }

    public void updateComment(Long commentId, String text) {
        commentRepository.updateText(commentId, text);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
