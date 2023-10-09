package com.cherniva.springsecurityserver.controller;

import com.cherniva.springsecurityserver.model.Pin;
import com.cherniva.springsecurityserver.model.Timestamp;
import com.cherniva.springsecurityserver.model.User;
import com.cherniva.springsecurityserver.repo.PinRepository;
import com.cherniva.springsecurityserver.repo.TimestampRepository;
import com.cherniva.springsecurityserver.service.PinService;
import com.cherniva.springsecurityserver.service.UserService;
import com.cherniva.springsecurityserver.utils.TimestampSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private PinService pinService;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private TimestampRepository timestampRepository;

    @GetMapping("/api/pins")
    String getListOfAllPins(Principal principal) {
        String ret = "";
        User user = userService.findByUsername(principal.getName());
        for(Pin pin : user.getPins()) {
            ret += "<" + pin.getValue() + "&" + pin.getPassword() + ">";
        }
        ret += "/";
        for(Pin pin : user.getDeletedPins()) {
            ret += "<" + pin.getValue() + "&" + pin.getPassword() + ">";
        }
//        ret += "/"; // as end of message
        user.setDeletedPins(Arrays.asList());
        userService.updateUser(user);
        return ret;
        //return ret.substring(0, ret.length()-1);
        //return  userService.findByUsername(principal.getName()).getPins().stream().map(pin -> pin.getValue()).toList();
    }

    private void updatePinsTimestamps(Pin pin, String timestamps) {
        List<Timestamp> currentList = pin.getTimestamps();
        for(String timestampS : timestamps.split("#")) {
            Timestamp timestamp = new Timestamp();
            System.out.println(timestampS);
            timestamp.setTimestamp(timestampS);
            timestampRepository.save(timestamp);
            currentList.add(timestamp);
        }
        Collections.sort(currentList, new TimestampSort());
        pin.setTimestamps(currentList);
        pinRepository.save(pin); // eq ti pinService.update(pin)
    }

    @PostMapping("/api/pins")
    void compareAndUpdate(@RequestBody String pins, Principal principal) {
//        System.out.println(pins);
        User user = userService.findByUsername(principal.getName());
        List<Pin> currentList = user.getPins();
        List<Pin> newList = new ArrayList<>();
        for(String data : pins.split(">")) { // possible form is <password1&uid1><password2&uid2>...
            String pass = data.substring(1).split("&")[0];
            String uid = data.substring(1).split("&")[1];
            Pin correspondingPin = null;
            for (Pin userPin : currentList) {
                if (pass.equals(userPin.getPassword())) {
                    correspondingPin = userPin;
                    break;
                }
            }
            if(correspondingPin == null) {
                correspondingPin = new Pin();
                correspondingPin.setPassword(pass);
                pinRepository.save(correspondingPin);
                currentList.add(correspondingPin);
            }
            if(uid.length() > 1) {
                correspondingPin.setUid(uid);
            }
            newList.add(correspondingPin);
//            if(uid.length() > 1) {
//                for (Pin userPin : currentList) {
//                    if (pass.equals(userPin.getPassword())) {
//                        userPin.setUid(uid);
//                        break;
//                    }
//                }
//            }
        }
        currentList = newList;
        for(String data : pins.split(">")) { // possible form is <password1&uid1&timestamps..>
            String pass = data.substring(1).split("&")[0];
            String uid = data.substring(1).split("&")[1];
            String timestamps = data.substring(1).split("&")[2];
            if(timestamps.length() > 1) {
                for (Pin userPin : currentList) {
                    if (pass.equals(userPin.getPassword()) || uid.equals(userPin.getUid())) {
                        updatePinsTimestamps(userPin, timestamps);
                        break;
                    }
                }
            }
        }

        user.setPins(currentList);
        userService.updateUser(user);
        //update list of pins and add to users list
    }
}
