package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.TestData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDataRepository extends JpaRepository<TestData, Long> {

}
