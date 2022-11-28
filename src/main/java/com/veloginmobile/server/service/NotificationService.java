package com.veloginmobile.server.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Notification;
import com.veloginmobile.server.data.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<String> sendByGroupName(String groupName, NotificationDto notificationDto) throws FirebaseMessagingException;
    void joinGroup(String groupName, String fcmToken);
}
