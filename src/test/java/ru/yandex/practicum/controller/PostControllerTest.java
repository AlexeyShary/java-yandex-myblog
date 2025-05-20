package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import ru.yandex.practicum.dto.PostDto;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.service.PostService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import(PostControllerTest.TestConfig.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static PostService mockPostService = mock(PostService.class);

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PostService postService() {
            return mockPostService;
        }
    }

    private PostDto samplePost() {
        return PostDto.builder()
                .id(1L)
                .title("Test")
                .likesCount(0)
                .textParts(List.of("line1", "line2"))
                .tags(List.of("java", "spring"))
                .comments(List.of(new CommentDto(1L, "comment")))
                .textPreview("line1")
                .build();
    }

    @BeforeEach
    void setup() {
        reset(mockPostService);
    }

    @Test
    void testGetPostsForm() throws Exception {
        when(mockPostService.getPosts(anyString(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(samplePost()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts", "paging", "search"));
    }

    @Test
    void testGetPostByIdForm() throws Exception {
        when(mockPostService.getPostById(1L)).thenReturn(samplePost());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void testCreatePost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "file.png", "image/png", "test".getBytes());

        mockMvc.perform(multipart("/posts")
                        .file(file)
                        .param("title", "Test")
                        .param("text", "Content")
                        .param("tags", "java"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(mockPostService).createPost(eq("Test"), eq("Content"), eq("java"), any());
    }

    @Test
    void testUpdatePost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "file.png", "image/png", "test".getBytes());

        mockMvc.perform(multipart("/posts/1")
                        .file(file)
                        .param("title", "Updated")
                        .param("text", "Text")
                        .param("tags", "spring")
                        .with(request -> { request.setMethod("POST"); return request; }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(mockPostService).updatePost(eq(1L), eq("Updated"), eq("Text"), eq("spring"), any());
    }

    @Test
    void testLikePost() throws Exception {
        mockMvc.perform(post("/posts/1/like").param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(mockPostService).likePost(1L, true);
    }

    @Test
    void testDeletePost() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(mockPostService).deletePost(1L);
    }
}