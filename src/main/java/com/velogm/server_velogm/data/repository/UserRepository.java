package com.velogm.server_velogm.data.repository;

import com.velogm.server_velogm.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUid(String uid);
}
