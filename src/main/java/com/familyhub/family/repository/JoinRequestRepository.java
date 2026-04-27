package com.familyhub.family.repository;

import com.familyhub.family.entity.FamilyJoinRequest;
import com.familyhub.family.entity.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JoinRequestRepository extends JpaRepository<FamilyJoinRequest, UUID> {

    boolean existsByFamilyIdAndUserIdAndStatus(UUID familyId, UUID userId, JoinRequestStatus status);

    @Query("SELECT jr FROM FamilyJoinRequest jr JOIN FETCH jr.user WHERE jr.family.id = :familyId AND jr.status = :status")
    List<FamilyJoinRequest> findByFamilyIdAndStatus(UUID familyId, JoinRequestStatus status);
}
