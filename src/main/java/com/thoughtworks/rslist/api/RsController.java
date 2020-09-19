package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.InvalidIndexError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.request.RsEventPatchRequest;
import com.thoughtworks.rslist.response.RsEventResponse;
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
    public ResponseEntity<List<RsEventResponse>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        List<RsEventResponse> rsEventList = new ArrayList<>();
        Stream.iterate(0, i -> i + 1).limit(rsEvents.size()).forEach(i -> {
            UserEntity user = rsEvents.get(i).getUser();
            rsEventList.add(RsEventResponse.builder()
                    .eventName(rsEvents.get(i).getEventName())
                    .keyWord(rsEvents.get(i).getKeyword())
                    .id(rsEvents.get(i).getId())
                    .votNum(rsEvents.get(i).getVoteNum())
                    .build());
        });
        if (start == null || end == null) {
            return ResponseEntity.ok(rsEventList);
        }
        return ResponseEntity.ok(rsEventList.subList(start - 1, end));
    }

    @GetMapping("/rs/{id}")
    public ResponseEntity<RsEventResponse> getRsEvent(@PathVariable int id) {
        Optional<RsEventEntity> result = rsEventRepository.findById(id);
        if (!result.isPresent()) {
            throw new InvalidIndexError();
        }
        RsEventEntity rsEvent = result.get();
        UserEntity user = rsEvent.getUser();

        return ResponseEntity.ok(RsEventResponse.builder()
                .eventName(rsEvent.getEventName())
                .keyWord(rsEvent.getKeyword())
                .id(rsEvent.getId())
                .votNum(rsEvent.getVoteNum())
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
    public ResponseEntity putRsEvent(@Valid @RequestBody RsEventPatchRequest rsEventPatchRequest, @PathVariable int id) {
        if (!userRepository.existsById(rsEventPatchRequest.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        String newName;
        String newKey;
        Optional<RsEventEntity> result = rsEventRepository.findById(id);
        RsEventEntity rsEvent = result.get();
        if (rsEventPatchRequest.getNewName() == null) {
            newName = rsEvent.getEventName();
            newKey = rsEventPatchRequest.getNewKey();
        } else if (rsEventPatchRequest.getNewKey() == null) {
            newName = rsEventPatchRequest.getNewName();
            newKey = rsEvent.getKeyword();
        } else {
            newName = rsEventPatchRequest.getNewName();
            newKey = rsEventPatchRequest.getNewKey();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .id(id)
                .eventName(newName)
                .keyword(newKey)
                .voteNum(rsEvent.getVoteNum())
                .user(UserEntity.builder()
                        .id(rsEventPatchRequest.getUserId())
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
