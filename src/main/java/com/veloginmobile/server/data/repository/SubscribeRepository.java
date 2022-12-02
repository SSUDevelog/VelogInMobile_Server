package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Subscribe;
import com.veloginmobile.server.data.entity.Target;
import com.veloginmobile.server.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository  extends JpaRepository<Subscribe, Long> {

    Subscribe findByUser(User user);
    Subscribe getByUserAndTarget(User user, Target target);
}