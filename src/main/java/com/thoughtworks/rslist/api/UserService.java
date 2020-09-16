package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@NoArgsConstructor
public class UserService {
    List<UserDto> userDtos = new ArrayList<>();
    List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList(){
        List<RsEvent> tempList = new ArrayList<>();
        tempList.add(new RsEvent("第一条事件","无分类"));
        tempList.add(new RsEvent("第二条事件","无分类"));
        tempList.add(new RsEvent("第三条事件","无分类"));
        return tempList;
    }
}
