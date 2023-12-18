package com.mss.app.service;

import com.mss.app.domain.User;

public interface LocalLogsService {
    void registerUser(User user);

    void updateUser(User user);
}
