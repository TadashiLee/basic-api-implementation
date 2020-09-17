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
    UserDto userDto1 = new UserDto("zhang","male",20,"wenchang.li@twuc.com","13308111111");
    UserDto userDto2 = new UserDto("wang","male",20,"wenchang.li@twuc.com","13308111111");
    UserDto userDto3 = new UserDto("li","male",20,"wenchang.li@twuc.com","13308111111");
    List<UserDto> userDtos = initUserDtos();
    List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList(){
        List<RsEvent> tempList = new ArrayList<>();

//        tempList.add(new RsEvent("第一条事件","无分类",userDto1));
//        tempList.add(new RsEvent("第二条事件","无分类",userDto2));
//        tempList.add(new RsEvent("第三条事件","无分类",userDto3));
        return tempList;
    }

    private List<UserDto> initUserDtos(){
        List<UserDto> tempList = new ArrayList<>();
        tempList.add(userDto1);
        tempList.add(userDto2);
        tempList.add(userDto3);
        return tempList;
    }

}
