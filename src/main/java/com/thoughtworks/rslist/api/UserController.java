package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public void register(@Valid @RequestBody UserDto userDto){
        userService.getUserDtos().add(userDto);
    }

}
