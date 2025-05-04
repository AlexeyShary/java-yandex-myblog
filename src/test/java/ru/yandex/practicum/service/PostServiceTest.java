package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PostTagRepository postTagRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void getPostsWithDataByTagTest() {
        Post post = new Post();
        post.setId(1L);
        post.setText("text\nline2");

        when(postRepository.findPaged(0, 10)).thenReturn(List.of(post));
        when(postTagRepository.findByPostId(1L)).thenReturn(List.of());
        when(commentRepository.findByPostId(1L)).thenReturn(List.of());

        List<PostDto> result = postService.getPosts(null, 0, 10);

        assertEquals(1, result.size());
        assertEquals("text", result.get(0).getTextPreview());
    }

    @Test
    void getPostsWithInvalidTagTest() {
        when(tagRepository.findByName("unknown")).thenReturn(Optional.empty());

        List<PostDto> result = postService.getPosts("unknown", 0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void getPostByIdWithDataTest() {
        Post post = Post.builder()
                .id(1L)
                .title("Title")
                .text("line1\nline2")
                .likesCount(5)
                .build();

        when(postRepository.findByPostId(1L)).thenReturn(Optional.of(post));
        when(postTagRepository.findByPostId(1L)).thenReturn(List.of());
        when(commentRepository.findByPostId(1L)).thenReturn(List.of());

        PostDto dto = postService.getPostById(1L);

        assertEquals("Title", dto.getTitle());
        assertEquals("line1", dto.getTextPreview());
        assertEquals(5, dto.getLikesCount());
    }

    @Test
    void getPostByIdWithoutDataTest() {
        when(postRepository.findByPostId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.getPostById(99L));
    }

    @Test
    void likePostAddLikeTest() {
        postService.likePost(1L, true);

        verify(postRepository).updateLikes(1L, 1);
    }

    @Test
    void likePostRemoveLikeTest() {
        postService.likePost(1L, false);

        verify(postRepository).updateLikes(1L, -1);
    }

    @Test
    void deletePostTest() {
        postService.deletePost(2L);

        verify(postRepository).deleteById(2L);
    }
}