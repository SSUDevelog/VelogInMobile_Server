package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Target getByVelogUserName(String velogUserName);
}
