package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Tag;
import com.veloginmobile.server.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    Tag findByUser(User user);
}
