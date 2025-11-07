package com.fil.pickple.persistence;

import com.fil.pickple.domain.BaseProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseProfileImageRepository extends JpaRepository<BaseProfileImage, Long> {
}
