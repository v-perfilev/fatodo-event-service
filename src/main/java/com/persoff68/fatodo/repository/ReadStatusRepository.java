package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    Optional<ReadStatus> findByUserId(UUID userId);

}
