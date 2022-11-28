package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlingRepository extends JpaRepository<Target, Long> {
    List<Target> findAll();
}
