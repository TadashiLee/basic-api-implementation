package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.exceptions.InvalidIndexError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    UserService userService;

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return ResponseEntity.ok(userService.rsList);
        }
            return ResponseEntity.ok(userService.rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index){
        if (index<1 || index>userService.rsList.size()){
            throw new InvalidIndexError();
        }
        return ResponseEntity.ok(userService.rsList.get(index-1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent){
        boolean verifyUserExist = false;
        for (int i = 0; i < userService.getUserDtos().size(); i++) {
            if (rsEvent.getUserDto().getName().equals(userService.getUserDtos().get(i).getName())){
                verifyUserExist = true;
                break;
            }
        }
        userService.rsList.add(rsEvent);
        if (!verifyUserExist){
            userService.userDtos.add(rsEvent.getUserDto());
        }
        return ResponseEntity.created(null).header("index", String.valueOf(userService.getRsList().size()-1)).build();
    }

    @PutMapping("/rs/event/{index}")
    public ResponseEntity putRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index){
        if (rsEvent.getKeyWord().equals("")){
            userService.rsList.get(index-1).setEventName(rsEvent.getEventName());
        }else if(rsEvent.getEventName().equals("")){
            userService.rsList.get(index-1).setKeyWord(rsEvent.getKeyWord());
        }else{
            userService.rsList.set(index-1, rsEvent);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/event/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index){
        if (index <= userService.rsList.size()){
            userService.rsList.remove(index-1);
        }
        return ResponseEntity.ok().build();
    }

}
