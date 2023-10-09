package com.cherniva.springsecurityserver.service;

import com.cherniva.springsecurityserver.dto.PinDto;
import com.cherniva.springsecurityserver.model.Pin;

import java.util.List;
public interface PinService {
    void savePin(PinDto pinDto);

    void updatePin(Pin pin);

    Pin findByValue(String value);
    Pin findByPassword(String password);

    Pin findByValueAndPassword(String value, String password);

    List<PinDto> findAllPins();
}
