package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {

    @Autowired
    UserService userService;

    @GetMapping("/rs/list")
    public List<RsEvent> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return userService.rsList;
        }
            return userService.rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getRsEvent(@PathVariable int index){
        return userService.rsList.get(index-1);
    }

    @PostMapping("/rs/event")
    public void addRsEvent(@Valid @RequestBody RsEvent rsEvent){

        userService.rsList.add(rsEvent);
    }

    @PutMapping("/rs/event/{index}")
    public void putRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index){
        if (rsEvent.getKeyWord().equals("")){
            userService.rsList.get(index-1).setEventName(rsEvent.getEventName());
        }else if(rsEvent.getEventName().equals("")){
            userService.rsList.get(index-1).setKeyWord(rsEvent.getKeyWord());
        }else{
            userService.rsList.set(index-1, rsEvent);
        }
    }

    @DeleteMapping("/rs/event/{index}")
    public void deleteRsEvent(@PathVariable int index){
        if (index <= userService.rsList.size()){
            userService.rsList.remove(index-1);
        }
    }

}
