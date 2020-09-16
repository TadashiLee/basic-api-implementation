package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/list")
    public List<UserDto> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return userService.userDtos;
        }
        return userService.userDtos.subList(start - 1, end);
    }

    @GetMapping("/user/{index}")
    public UserDto getRsEvent(@PathVariable int index){
        return userService.userDtos.get(index-1);
    }

    @PostMapping("/user/register")
    public void register(@Valid @RequestBody UserDto userDto){
        userService.getUserDtos().add(userDto);
    }

}
