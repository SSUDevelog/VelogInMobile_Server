package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Crawling;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrawlingRepository extends JpaRepository<Crawling, Long> {
    List<Crawling> findAll();
}
