package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.InvalidIndexError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class RsController {

    @Autowired
    UserService userService;
    private final UserRepository userRepository;
    private final RsEventRepository rsEventRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }


    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        List<RsEvent> rsEventList = new ArrayList<>();
        Stream.iterate(0, i -> i + 1).limit(rsEvents.size()).forEach(i -> {
            UserEntity user = rsEvents.get(i).getUser();
            rsEventList.add(RsEvent.builder()
                    .eventName(rsEvents.get(i).getEventName())
                    .keyWord(rsEvents.get(i).getKeyword())
                    .userDto(new UserDto(
                            user.getUserName(),
                            user.getGender(),
                            user.getAge(),
                            user.getEmail(),
                            user.getPhone()))
                    .build());
        });
        if (start == null || end == null) {
            return ResponseEntity.ok(rsEventList);
        }
        return ResponseEntity.ok(rsEventList.subList(start - 1, end));
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int id) {
        Optional<RsEventEntity> result = rsEventRepository.findById(id);
        if (!result.isPresent()) {
            throw new InvalidIndexError();
        }
        RsEventEntity rsEvent = result.get();
        UserEntity user = rsEvent.getUser();

        return ResponseEntity.ok(RsEvent.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyword())
                .userDto(new UserDto(
                        user.getUserName(),
                        user.getGender(),
                        user.getAge(),
                        user.getEmail(),
                        user.getPhone()))
                .build());
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
        if (!userRepository.existsById(rsEvent.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord())
                .user(UserEntity.builder()
                        //为什么只有一个参数
                        .id(rsEvent.getUserId())
                        .build())
                .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null)
                .header("userId", String.valueOf(rsEventEntity.getUser().getId())).build();
    }

    @PutMapping("/rs/event/{index}")
    public ResponseEntity putRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index) {
        if (rsEvent.getKeyWord().equals("")) {
            userService.rsList.get(index - 1).setEventName(rsEvent.getEventName());
        } else if (rsEvent.getEventName().equals("")) {
            userService.rsList.get(index - 1).setKeyWord(rsEvent.getKeyWord());
        } else {
            userService.rsList.set(index - 1, rsEvent);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/event/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        if (index <= userService.rsList.size()) {
            userService.rsList.remove(index - 1);
        }
        return ResponseEntity.ok().build();
    }

}
