package com.veloginmobile.server.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.veloginmobile.server.data.dto.notification.NotificationDto;
import com.veloginmobile.server.data.repository.NotificationRepository;
import com.veloginmobile.server.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    @Value("${fcm.key.scope}")
    private String fireBaseScope;

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                                    .createScoped(List.of(fireBaseScope)))
                    .build();
            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                LOGGER.info("Firebase application has been initialized");
            }
        } catch (IOException e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void joinGroup(String groupName, String fcmToken){
        com.veloginmobile.server.data.entity.Notification notification = notificationRepository.getByGroupName(groupName);
        if(notification == null){
            notification = new com.veloginmobile.server.data.entity.Notification();
            notification.setGroupName(groupName);
        }

        if (!notification.getReceivers().contains(fcmToken)) {
            notification.getReceivers().add(fcmToken);
            notificationRepository.save(notification);
        }
//        notification.getReceivers().add(fcmToken);
//        notificationRepository.save(notification);
    }

    @Override
    public List<String> sendByGroupName(String groupName, NotificationDto notificationDto) throws FirebaseMessagingException{
        List<String> tokenList = notificationRepository.getByGroupName(groupName).getReceivers();

        List<Message> messages = tokenList.stream().map(token -> Message.builder()
                .putData("link", notificationDto.getLink())//알림 누르면 이동하는 곳!
                .setNotification(new Notification(notificationDto.getTitle(), notificationDto.getBody()))
                .setToken(token)
                .build()).collect(Collectors.toList());

        BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
        List<String> failedTokens = new ArrayList<>();

        if(response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();

            for(int i=0; i< responses.size(); i++) {
                if(!responses.get(i).isSuccessful()){
                    failedTokens.add(tokenList.get(i));
                }
            }
            LOGGER.error("List of tokens are not valid FCM token : " + failedTokens);
        }

        return failedTokens;
    }
}
