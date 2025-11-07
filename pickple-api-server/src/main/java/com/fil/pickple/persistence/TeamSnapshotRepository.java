package com.fil.pickple.persistence;

import com.fil.pickple.domain.TeamSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamSnapshotRepository extends JpaRepository<TeamSnapshot, Long> {
}
