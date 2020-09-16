package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return ResponseEntity.ok(userService.userDtos);
        }
        return ResponseEntity.ok(userService.userDtos.subList(start - 1, end));
    }

    @GetMapping("/user/{index}")
    public ResponseEntity<UserDto> getRsEvent(@PathVariable int index){
        return ResponseEntity.ok(userService.userDtos.get(index-1));
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto){
        userService.getUserDtos().add(userDto);
        return ResponseEntity.created(null).build();
    }

}
