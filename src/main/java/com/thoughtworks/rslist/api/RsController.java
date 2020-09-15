package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList(){
        List<RsEvent> tempList = new ArrayList<>();
        tempList.add(new RsEvent("第一条事件","无分类"));
        tempList.add(new RsEvent("第二条事件","无分类"));
        tempList.add(new RsEvent("第三条事件","无分类"));
        return tempList;
    }
    @GetMapping("/rs/list")
    public List<RsEvent> getAllRsEvent(@RequestParam(required = false) Integer start
            , @RequestParam(required = false) Integer end) {
        if (start == null || end == null){
            return rsList;
        }
            return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/{index}")
    public RsEvent getRsEvent(@PathVariable int index){
        return rsList.get(index-1);
    }

    @PostMapping("/rs/event")
    public void addRsEvent(@RequestBody RsEvent rsEvent){
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/event/{index}")
    public void putRsEvent(@RequestBody RsEvent rsEvent, @PathVariable int index){
        if (rsEvent.getEventName().equals("")){
            rsList.get(index-1).setKeyWord(rsEvent.getKeyWord());
        }else {
            rsList.set(index-1, rsEvent);
        }
    }

    @DeleteMapping("/rs/event/{index}")
    public void deleteRsEvent(@PathVariable int index){
        if (index <= rsList.size()){
            rsList.remove(index-1);
        }
    }

}
