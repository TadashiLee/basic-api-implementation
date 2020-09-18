package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.exceptions.InvalidIndexError;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    UserService userService;
    //    private final UserRepository userRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RsEventRepository rsEventRepository;



    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(userService.rsList);
        }
        return ResponseEntity.ok(userService.rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
        if (index < 1 || index > userService.rsList.size()) {
            throw new InvalidIndexError();
        }
        return ResponseEntity.ok(userService.rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) {
        if(!userRepository.existsById(rsEvent.getUserId())){
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
//        .header("index", String.valueOf(userService.getRsList().size() - 1)).build();
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
