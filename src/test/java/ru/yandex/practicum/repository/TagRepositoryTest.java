package ru.yandex.practicum.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.model.Tag;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void saveAndFindByIdTest() {
        Tag entity = new Tag();
        entity.setName("Test tag");

        Tag saved = tagRepository.save(entity);
        Optional<Tag> found = tagRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test tag");
    }
}