package com.cherniva.springsecurityserver.service;

import com.cherniva.springsecurityserver.dto.TimestampDto;
import com.cherniva.springsecurityserver.dto.UserDto;
import com.cherniva.springsecurityserver.model.Timestamp;
import com.cherniva.springsecurityserver.model.User;

import java.util.List;

public interface TimestampService {
    void saveTimestamp(TimestampDto timestampDto);

    void updateTimestamp(Timestamp timestamp);

    Timestamp findByTimestamp(String timestamp);

    List<TimestampDto> findAllTimestamps();
}
