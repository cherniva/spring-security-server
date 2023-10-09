package com.cherniva.springsecurityserver.controller;

import com.cherniva.springsecurityserver.dto.PinDto;
import com.cherniva.springsecurityserver.dto.UserDto;
import com.cherniva.springsecurityserver.model.Pin;
import com.cherniva.springsecurityserver.model.Timestamp;
import com.cherniva.springsecurityserver.model.User;
import com.cherniva.springsecurityserver.service.PinService;
import com.cherniva.springsecurityserver.service.UserService;
import com.cherniva.springsecurityserver.utils.SortByDate;
import com.cherniva.springsecurityserver.utils.SortByUser;
import com.cherniva.springsecurityserver.utils.TimestampSort;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;
import java.util.*;

@Controller
public class MainController {

    private final UserService userService;

    private final PinService pinService;

    public MainController(UserService userService, PinService pinService) {
        this.userService = userService;
        this.pinService = pinService;
    }

    @GetMapping("/")
    public String home() {return "login";}

//    @GetMapping("index")
//    public String home() {
//        return "index";
//    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model) {
        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/timestamps")
    public String getTimestamps(Principal principal, Model model,
                                @RequestParam(value = "redirect", required = true) int index) throws ParseException {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user.getPins().get(index).getValue());
        List<Timestamp> timestampList = user.getPins().get(index).getTimestamps();
        model.addAttribute("list", timestampList);

        return "timestamps";
    }

    private void welcomePageFill(Model model, User user, PinDto pinDto) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("pins", user.getPins());
        model.addAttribute("new_pin", pinDto);
    }

    @GetMapping("/welcome")
    public String welcomePage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
//        for(Pin el : user.getPins()) {
//            System.out.println(el.getValue());
//        }
        welcomePageFill(model, user, new PinDto());
        return "welcome";
    }

    @GetMapping("/sortByUser")
    public String test1(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Pin> currentList = user.getPins();

        Collections.sort(currentList, new SortByUser());

        user.setPins(currentList);
        userService.updateUser(user);
        welcomePageFill(model, userService.findByUsername(principal.getName()), new PinDto());
        return "redirect:/welcome";
    }

    @GetMapping("/sortByInteraction")
    public String test2(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Pin> currentList = user.getPins();

        Collections.sort(currentList, new SortByDate());

        user.setPins(currentList);
        userService.updateUser(user);
        welcomePageFill(model, userService.findByUsername(principal.getName()), new PinDto());
        return "redirect:/welcome";
    }

    @PostMapping("/welcome")
    public String welcomePageAdd(/*@Valid */@ModelAttribute("new_pin") PinDto pinDto,
                                 @RequestParam(value = "action", required = true) String action,
                                 BindingResult result,
                                 Principal principal,
                                 Model model) {
        User user = userService.findByUsername(principal.getName());
        // check '<', '>', '&', '/' and may be more
        if(pinDto.getValue().isEmpty()) {
            result.rejectValue("value", null, "Value should not be empty");
            welcomePageFill(model, user, pinDto);
            return "/welcome";
        }
        if(pinDto.getPassword().isEmpty()) {
            result.rejectValue("password", null, "Password should not be empty");
            welcomePageFill(model, user, pinDto);
            return "/welcome";
        }
        if(pinDto.getPassword().length() != 6) {
            result.rejectValue("password", null, "Password should be 6 character long");
            welcomePageFill(model, user, pinDto);
            return "/welcome";
        }
//        Pin existing = pinService.findByValue(pinDto.getValue()); // change to search buy key and password
        Pin existing = pinService.findByValueAndPassword(pinDto.getValue(), pinDto.getPassword());
        List<Pin> currentList = user.getPins();
        List <Pin> currentDeletePinsList = user.getDeletedPins();
        if(action.equals("update")) {
            for(Pin pin: currentList) {
                if(pin.getPassword().equals(pinDto.getPassword())) {
                    pin.setValue(pinDto.getValue());
                    break;
                }
            }
        }
        if (action.equals("add")) {
            if (existing == null) {
                pinService.savePin(pinDto);
            }
            String value = pinDto.getValue();
            String password = pinDto.getPassword();
//            boolean valueWasUsed = false; // I'll allow to use one nickname more than once
                                            // to keep an option to add more passwords for one person
            boolean passwordWasUsed = false;
            for(Pin pin : currentList) {
                if(pin.getPassword().equals(password)) {
                    passwordWasUsed = true;
                    break;
                }
            }
            if(passwordWasUsed) {
                result.rejectValue("password", null, "Password already in use");
            } else if (currentList.contains(existing)) { // useless now
                result.rejectValue("value", null, "User has been already added");
            } else {
                currentList.add(pinService.findByValueAndPassword(pinDto.getValue(), pinDto.getPassword()));
            }
        }
        if (action.equals("delete")) {
           if(existing == null) {
               result.rejectValue("value", null, "User do not exist");
           }
           if(!currentList.contains(existing)) {
               result.rejectValue("value", null, "No such User in your list");
           } else {
               currentList.remove(pinService.findByValueAndPassword(pinDto.getValue(), pinDto.getPassword()));
               currentDeletePinsList.add(pinService.findByValueAndPassword(pinDto.getValue(), pinDto.getPassword()));
           }
        }

        if (result.hasErrors()) {
            welcomePageFill(model, user, pinDto);
            return "/welcome";
        }
        user.setPins(currentList);
        user.setDeletedPins(currentDeletePinsList);
        userService.updateUser(user);
        return "redirect:/welcome";
    }
}
