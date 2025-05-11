package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.repository.CommentRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void addCommentTest() {
        Long postId = 1L;
        String text = "Test comment";

        commentService.addComment(postId, text);

        verify(commentRepository).save(postId, text);
    }

    @Test
    void updateCommentTest() {
        Long commentId = 2L;
        String updatedText = "Updated comment";

        commentService.updateComment(commentId, updatedText);

        verify(commentRepository).updateText(commentId, updatedText);
    }

    @Test
    void deleteCommentTest() {
        Long commentId = 3L;

        commentService.deleteComment(commentId);

        verify(commentRepository).deleteById(commentId);
    }
}