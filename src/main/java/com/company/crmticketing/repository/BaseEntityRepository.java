package com.company.crmticketing.repository;

import com.company.crmticketing.model.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    default Optional<T> findActiveById(ID id) {
        return findById(id).filter(entity -> !entity.isDeleted());
    }

    default Optional<T> findByIdIncludingDeleted(ID id) {
        return findById(id);
    }

    default List<T> findAllActive() {
        return findAll().stream()
                .filter(entity -> !entity.isDeleted())
                .toList();
    }

    default int softDeleteByIdWithVersion(ID id, LocalDateTime now, Long currentVersion) {
        T entity = findByIdIncludingDeleted(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        if (entity.isDeleted()) {
            return 0;
        }

        if (entity.getVersion() != null && !entity.getVersion().equals(currentVersion)) {
            return 0;
        }

        entity.setDeleted(true);
        entity.setDeletedAt(now);
        save(entity);
        return 1;
    }


    default T updateWithConflictMessage(ID id, Consumer<T> updateLogic) {
        try {
            T entity = findActiveById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

            updateLogic.accept(entity);
            return save(entity);

        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException(
                    "This record is being modified by another user. Please refresh and try again."
            );
        }
    }

    default boolean softDelete(ID id) {
        return softDeleteWithRetry(id, 3);
    }

    default boolean softDeleteWithRetry(ID id, int maxRetries) {
        int retries = 0;
        while (retries < maxRetries) {
            try {
                T entity = findByIdIncludingDeleted(id)
                        .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

                if (entity.isDeleted()) {
                    return false;
                }

                entity.setDeleted(true);
                entity.setDeletedAt(LocalDateTime.now());
                save(entity);

                return true;

            } catch (OptimisticLockException e) {
                retries++;

                if (retries >= maxRetries) {
                    throw new RuntimeException("Failed to soft delete after " + maxRetries + " retries", e);
                }

                try {
                    Thread.sleep(100L * retries);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
            }
        }
        return false;
    }

    default long countActive() {
        return findAll().stream()
                .filter(entity -> !entity.isDeleted())
                .count();
    }
}
