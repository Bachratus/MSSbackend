package com.mss.app.service.impl;

import org.springframework.stereotype.Service;

import com.mss.app.domain.User;
import com.mss.app.service.LocalLogsService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LocalLogsServiceImpl implements LocalLogsService {

    private static final String LOGS_FILE_PATH = "logs.txt";

    @Override
    public void registerUser(User user) {
        String logEntry = createLogEntry(user, "User added");
        appendToLogFile(logEntry);
    }

    @Override
    public void updateUser(User user) {
        String logEntry = createLogEntry(user, "User edited");
        appendToLogFile(logEntry);
    }

    private String createLogEntry(User user, String operation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        return String.format("%s - Time: %s -> [ ID: %s, Login: %s, Name: %s, Surname: %s, Email: %s ]%n",
                operation,
                formattedDateTime,
                user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }

    private void appendToLogFile(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(LOGS_FILE_PATH).toFile(), true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
