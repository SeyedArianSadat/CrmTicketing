package com.company.crmticketing.service;

import com.company.crmticketing.model.BaseEntity;
import com.company.crmticketing.repository.BaseEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseEntityServiceTest {

    private final BaseEntityRepository<TestEntity, Long> repository = mock();
    private final TestService service = new TestService(repository);

    @Test
    void findDtoByIdReturnsMappedDtoForActiveEntity() {
        TestEntity entity = entity("first");
        when(repository.findActiveById(1L)).thenReturn(Optional.of(entity));

        TestDto dto = service.findDtoById(1L);

        assertThat(dto.name()).isEqualTo("first");
    }

    @Test
    void findDtoByIdThrowsWhenEntityIsNotActive() {
        when(repository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findDtoById(404L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("TestEntity not found with id: 404");
    }

    @Test
    void findAllDtosMapsAllActiveEntities() {
        when(repository.findAllActive()).thenReturn(List.of(entity("first"), entity("second")));

        List<TestDto> dtos = service.findAllDtos();

        assertThat(dtos).extracting(TestDto::name)
                .containsExactly("first", "second");
    }

    @Test
    void updateFromDtoCopiesCoreFieldsFromExistingEntity() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(2);
        TestEntity existing = entity("old");
        existing.setCreatedAt(createdAt);
        existing.setCreatedBy("creator");
        existing.setDeleted(true);
        existing.setDeletedAt(LocalDateTime.now().minusDays(1));
        existing.setDeletedBy(10L);
        existing.setVersion(7L);

        when(repository.findActiveById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(TestEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TestDto dto = service.updateFromDto(1L, current -> current.name = "new");

        assertThat(dto.name()).isEqualTo("new");
        verify(repository).save(org.mockito.ArgumentMatchers.argThat(updated ->
                updated.getCreatedAt().equals(createdAt)
                        && updated.getCreatedBy().equals("creator")
                        && updated.isDeleted()
                        && updated.getDeletedBy().equals(10L)
                        && updated.getVersion().equals(7L)
        ));
    }

    @Test
    void restoreReturnsFalseWhenEntityIsNotDeleted() {
        TestEntity entity = entity("active");
        when(repository.findByIdIncludingDeleted(1L)).thenReturn(Optional.of(entity));

        boolean restored = service.restore(1L);

        assertThat(restored).isFalse();
    }

    @Test
    void restoreClearsDeleteMarkersAndSavesDeletedEntity() {
        TestEntity entity = entity("deleted");
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now());
        entity.setDeletedBy(25L);
        when(repository.findByIdIncludingDeleted(1L)).thenReturn(Optional.of(entity));

        boolean restored = service.restore(1L);

        assertThat(restored).isTrue();
        assertThat(entity.isDeleted()).isFalse();
        assertThat(entity.getDeletedAt()).isNull();
        assertThat(entity.getDeletedBy()).isNull();
        verify(repository).save(entity);
    }

    private static TestEntity entity(String name) {
        TestEntity entity = new TestEntity();
        entity.name = name;
        return entity;
    }

    private static final class TestService extends BaseEntityService<TestEntity, Long, TestDto> {
        private TestService(BaseEntityRepository<TestEntity, Long> repository) {
            super(repository, entity -> new TestDto(entity.name), dto -> entity(dto.name));
        }

        @Override
        protected String getEntityTypeName() {
            return "TestEntity";
        }
    }

    private static final class TestEntity extends BaseEntity {
        private String name;
    }

    private static final class TestDto {
        private String name;

        private TestDto(String name) {
            this.name = name;
        }

        private String name() {
            return name;
        }
    }
}
