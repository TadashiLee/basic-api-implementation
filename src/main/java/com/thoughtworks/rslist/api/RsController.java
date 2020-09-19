package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.InvalidIndexError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.response.RsEventResponse;
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
                        .id(rsEvent.getUserId())
                        .build())
                .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null)
                .header("userId", String.valueOf(rsEventEntity.getUser().getId())).build();
    }

    @PatchMapping("/rs/event/{id}")
    public ResponseEntity putRsEvent(@Valid @RequestBody RsEventResponse rsEventResponse, @PathVariable int id) {
        if (!userRepository.existsById(rsEventResponse.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        String newName;
        String newKey;
        if (rsEventResponse.getNewName()==null) {
            Optional<RsEventEntity> result = rsEventRepository.findById(id);
            RsEventEntity rsEvent = result.get();
            newName = rsEvent.getEventName();
            newKey = rsEventResponse.getNewKey();
        } else if (rsEventResponse.getNewKey()==null) {
            Optional<RsEventEntity> result = rsEventRepository.findById(id);
            RsEventEntity rsEvent = result.get();
            newName = rsEventResponse.getNewName();
            newKey = rsEvent.getKeyword();
        } else {
            newName = rsEventResponse.getNewName();
            newKey = rsEventResponse.getNewKey();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .id(id)
                .eventName(newName)
                .keyword(newKey)
                .user(UserEntity.builder()
                        .id(rsEventResponse.getUserId())
                        .build())
                .build();
        rsEventRepository.save(rsEventEntity);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/event/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int id) {
        if (!rsEventRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        rsEventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
