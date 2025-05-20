package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.model.Comment;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void saveAndFindByIdTest() {
        Comment entity = new Comment();
        entity.setText("Test comment");

        Comment saved = commentRepository.save(entity);
        Optional<Comment> found = commentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getText()).isEqualTo("Test comment");
    }
}