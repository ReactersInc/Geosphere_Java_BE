package com.tridev.geoSphere.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Slf4j
@Service
public class FirebaseConfig {

    @Value("${firebase.private.key.path}")
    private String firebasePrivateKey;

    @PostConstruct
    public void initialize() {
        try {
            FileInputStream serviceAccount = new FileInputStream(firebasePrivateKey);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("✅ Firebase has been successfully initialized.");
            } else {
                log.info("ℹ️ Firebase was already initialized.");
            }

            // Optional: List all apps (should be just one usually)
            FirebaseApp.getApps().forEach(app -> log.info("Firebase App Name: {}", app.getName()));

        } catch (Exception e) {
            log.error("❌ Failed to initialize Firebase", e);
        }
    }
}
