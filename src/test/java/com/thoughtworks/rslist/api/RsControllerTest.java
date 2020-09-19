package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.response.RsEventResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_get_rs_list() throws Exception {

        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        UserEntity user1 = saveOneUserEntity("lee", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent1 = saveOneRsEventEntity("event 1", "key", user1);

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].userDto.name", is("Tadashi")))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].userDto.name", is("lee")));
    }

    @Test
    void should_get_rs_list_by_range() throws Exception {

        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        UserEntity user1 = saveOneUserEntity("lee", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent1 = saveOneRsEventEntity("event 1", "key", user1);

        mockMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].userDto.name", is("Tadashi")))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].userDto.name", is("lee")));
    }

    @Test
    void should_get_error_by_error_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_get_one_event() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);
        mockMvc.perform(get("/rs/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.userDto.name", is("Tadashi")));
    }

    @Test
    void should_get_error_by_error_userid() throws Exception {
        mockMvc.perform(get("/rs/{id}", 5))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    public void should_add_rsEvent_when_user_exists() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        UserDto userDto = new UserDto("Tadashi", "male", 20, "123@twu.com", "13308375411");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto, user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("userId","1"));
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(1, rsEvents.size());
        assertEquals("猪肉涨价了", rsEvents.get(0).getEventName());
        assertEquals(user.getId(), rsEvents.get(0).getUser().getId());
    }

    @Test
    public void should_not_add_rsEvent_when_eventName_is_epmpty() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        UserDto userDto = new UserDto("Tadashi", "male", 20, "123@twu.com", "13308375411");
        RsEvent rsEvent = new RsEvent("", "经济", userDto, user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    public void should_not_add_rsEvent_when_user_not_exists() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        UserDto userDto = new UserDto("Tadashi", "male", 20, "123@twu.com", "13308375411");
        int anotheId = 2;

        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto, anotheId);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_delete_user() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        mockMvc.perform(delete("/user/event/{id}", user.getId()))
                .andExpect(status().isNoContent());
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        List<UserEntity> users = userRepository.findAll();
        assertEquals(0, users.size());
        assertEquals(0, rsEvents.size());
    }

    @Test
    public void delet_rsEvent_by_id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        mockMvc.perform(delete("/rs/event/{id}", rsEvent.getId()))
                .andExpect(status().isNoContent());
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0,rsEvents.size());
    }

    @Test
    public void should_update_rsEvent_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventResponse rsEventResponse = new RsEventResponse("new event", "new key", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventResponse);

        mockMvc.perform(patch("/rs/event/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("new event")))
                .andExpect(jsonPath("$.keyWord", is("new key")))
                .andExpect(jsonPath("$.userDto.name", is("Tadashi")));

    }
    @Test
    public void should_just_update_rsEvent_keyWord_if_rsEvent_name_is_null_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventResponse rsEventResponse = new RsEventResponse(null, "new key", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventResponse);

        mockMvc.perform(patch("/rs/event/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("new key")))
                .andExpect(jsonPath("$.userDto.name", is("Tadashi")));

    }

    @Test
    public void should_just_update_rsEvent_name_if_keyWord_is_null_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventResponse rsEventResponse = new RsEventResponse("new event", null, user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventResponse);

        mockMvc.perform(patch("/rs/event/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("new event")))
                .andExpect(jsonPath("$.keyWord", is("key")))
                .andExpect(jsonPath("$.userDto.name", is("Tadashi")));

    }

    private RsEventEntity saveOneRsEventEntity(String eventName, String keyWord, UserEntity user){
        RsEventEntity rsEvent = RsEventEntity.builder()
                .eventName(eventName)
                .keyword(keyWord)
                .user(user)
                .build();
        rsEventRepository.save(rsEvent);
        return rsEvent;
    }

    private UserEntity saveOneUserEntity(String userName, String gender, int age, String phone, String email, int voteNum){
        UserEntity user = UserEntity.builder()
                .userName(userName)
                .gender(gender)
                .age(age)
                .phone(phone)
                .email(email)
                .voteNum(voteNum)
                .build();
        userRepository.save(user);
        return user;
    }


//    @Test
//    void should_edit_a_rs_event() throws Exception {
//        UserDto userDto = new UserDto("zhang", "male", 21, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[0].keyWord", is("经济")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
//    }
//
//    @Test
//    void should_edit_a_rs_event_just_input_keyword() throws Exception {
//        UserDto userDto = new UserDto("zhang", "male", 21, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("经济")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
//    }
//
//    @Test
//    void should_edit_a_rs_event_just_input_eventName() throws Exception {
//        UserDto userDto = new UserDto("zhang", "male", 21, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
//    }

}
