package com.veloginmobile.server.data.repository;

import com.veloginmobile.server.data.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification getByGroupName(String groupName);
}
