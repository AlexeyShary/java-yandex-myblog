package ru.yandex.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TagRepository tagRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("Sample Post")
                .text("Line 1\\nLine 2")
                .likesCount(5)
                .imageUrl("image.png")
                .tags(new HashSet<>())
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    void getPostsTest() {
        when(postRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(post)));
        Page<PostDto> result = postService.getPosts(null, 0, 10);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Sample Post");
    }

    @Test
    void getPostByIdTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        PostDto dto = postService.getPostById(1L);
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void createPostTest() {
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArgument(0));
        when(postRepository.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "test".getBytes());
        postService.createPost("New", "Content", "java spring", image);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void updatePostTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(i -> i.getArgument(0));

        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "test".getBytes());
        postService.updatePost(1L, "Updated", "Updated text", "java", image);

        assertThat(post.getTitle()).isEqualTo("Updated");
        verify(postRepository).save(post);
    }

    @Test
    void likePostTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.likePost(1L, true);
        assertEquals(6, post.getLikesCount());
        verify(postRepository).save(post);
    }

    @Test
    void deletePostTest() {
        postService.deletePost(1L);
        verify(postRepository).deleteById(1L);
    }

    @Test
    void getMissingImageTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(Post.builder().id(1L).build()));
        ResponseEntity<Resource> response = postService.getImageResponse(1L);
        assertEquals(204, response.getStatusCode().value());
    }
}