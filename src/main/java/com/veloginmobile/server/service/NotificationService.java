package com.veloginmobile.server.service;

import com.google.firebase.messaging.Notification;
import com.veloginmobile.server.data.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {
    void sendByGroupName(String groupName, NotificationDto notificationDto);
    void joinGroup(String groupName, String fcmToken);
}
