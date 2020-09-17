package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.CommentError;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/list")
    public ResponseEntity<List<UserDto>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return ResponseEntity.ok(userService.userDtos);
        }
        return ResponseEntity.ok(userService.userDtos.subList(start - 1, end));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getRsEvent(@PathVariable int id){
        Optional<UserEntity> result = userRepository.findById(id);
        if (!result.isPresent()){
            throw new RuntimeException();
        }
        UserEntity user = result.get();

        return ResponseEntity.ok(UserDto.builder()
                .name(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .vote(user.getVoteNum())
                .build());
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto){
//        userService.getUserDtos().add(userDto);
        UserEntity userEntity = UserEntity.builder()
                .userName(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .voteNum(userDto.getVote())
                .build();
        userRepository.save(userEntity);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/user/event/{id}")
    public ResponseEntity deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommentError> handlerExceptions(Exception ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.badRequest().body(commentError);
    }
}
