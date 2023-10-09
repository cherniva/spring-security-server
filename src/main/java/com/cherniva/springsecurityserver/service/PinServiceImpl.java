package com.cherniva.springsecurityserver.service;

import com.cherniva.springsecurityserver.dto.PinDto;
import com.cherniva.springsecurityserver.dto.UserDto;
import com.cherniva.springsecurityserver.model.Pin;
import com.cherniva.springsecurityserver.model.User;
import com.cherniva.springsecurityserver.repo.PinRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PinServiceImpl implements PinService {
    private PinRepository pinRepository;

    public PinServiceImpl(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    @Override
    public void savePin(PinDto pinDto) {
        Pin pin = new Pin();
        pin.setValue(pinDto.getValue());
        pin.setPassword(pinDto.getPassword());
        pin.setUid(null);
        pinRepository.save(pin);
    }

    @Override
    public void updatePin(Pin pin) {
        pinRepository.save(pin);
    }

    @Override
    public Pin findByValue(String value) {
        return pinRepository.findByValue(value);
    }

    @Override
    public Pin findByPassword(String password) {
        return pinRepository.findByPassword(password);
    }

    @Override
    public Pin findByValueAndPassword(String value, String password) {
        return pinRepository.findByValueAndPassword(value, password);
    }

    @Override
    public List<PinDto> findAllPins() {
        List<Pin> pins = pinRepository.findAll();
        return pins.stream().map((pin) -> convertEntityToDto(pin))
                .collect(Collectors.toList());
    }
    private PinDto convertEntityToDto(Pin pin){
        PinDto pinDto = new PinDto();
        String value = pin.getValue();
        String password = pin.getPassword();
        String uid = pin.getUid();
        pinDto.setValue(value);
        pinDto.setValue(password);
        pinDto.setValue(uid);

        return pinDto;
    }
}
