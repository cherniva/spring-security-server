package com.cherniva.springsecurityserver.service;

import com.cherniva.springsecurityserver.dto.TimestampDto;
import com.cherniva.springsecurityserver.dto.UserDto;
import com.cherniva.springsecurityserver.model.Timestamp;
import com.cherniva.springsecurityserver.model.User;
import com.cherniva.springsecurityserver.repo.TimestampRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimestampServiceImpl implements TimestampService {

    private TimestampRepository timestampRepository;

    public TimestampServiceImpl(TimestampRepository timestampRepository) {
        this.timestampRepository = timestampRepository;
    }

    @Override
    public void saveTimestamp(TimestampDto timestampDto) {
        Timestamp timestamp = new Timestamp();
        timestamp.setTimestamp(timestamp.getTimestamp());
        timestampRepository.save(timestamp);
    }

    @Override
    public void updateTimestamp(Timestamp timestamp) {
        timestampRepository.save(timestamp);
    }

    @Override
    public Timestamp findByTimestamp(String timestamp) {
        return timestampRepository.findByTimestamp(timestamp);
    }

    private TimestampDto convertEntityToDto(Timestamp timestamp){
        TimestampDto timestampDto = new TimestampDto();
        String time = timestamp.getTimestamp();
        timestampDto.setTimestamp(time);
        return timestampDto;
    }

    @Override
    public List<TimestampDto> findAllTimestamps() {
        List<Timestamp> timestamps = timestampRepository.findAll();
        return timestamps.stream().map((timestamp) -> convertEntityToDto(timestamp))
                .collect(Collectors.toList());
    }
}
