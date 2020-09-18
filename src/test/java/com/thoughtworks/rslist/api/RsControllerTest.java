package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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

//    @Test
//    public void should_add_rsEvent_when_user_exists() throws Exception {
//        UserEntity user = UserEntity.builder()
//                .userName("Tadashi")
//                .gender("male")
//                .age(20)
//                .phone("13308375411")
//                .email("123@twu.com")
//                .voteNum(10)
//                .build();
//        userRepository.save(user);
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user.getId());
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
//        assertEquals(1, rsEvents.size());
//        assertEquals("猪肉涨价了", rsEvents.get(0).getEventName());
//        assertEquals(user.getId(), rsEvents.get(0).getUserId());
//    }

//    @Test
//    public void should_not_add_rsEvent_when_user_not_exists() throws Exception {
//        UserEntity user = UserEntity.builder()
//                .userName("Tadashi")
//                .gender("male")
//                .age(20)
//                .phone("13308375411")
//                .email("123@twu.com")
//                .voteNum(10)
//                .build();
//        userRepository.save(user);
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", 2);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event")
//                .content(json)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void should_delete_user() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("Tadashi")
                .gender("male")
                .age(20)
                .phone("13308375411")
                .email("123@twu.com")
                .voteNum(10)
                .build();
        userRepository.save(user);

        RsEventEntity rsEvent = RsEventEntity.builder()
                .eventName("event 0")
                .keyword("key")
                .userId(user.getId())
                .build();

        rsEventRepository.save(rsEvent);

        mockMvc.perform(delete("/user/event/{id}", user.getId()))
                .andExpect(status().isNoContent());
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        List<UserEntity> users = userRepository.findAll();

        assertEquals(0, users.size());
        assertEquals(0, rsEvents.size());

    }
    @Test
    void should_get_one_event() throws Exception {
        UserEntity user = UserEntity.builder()
                .userName("Tadashi")
                .gender("male")
                .age(20)
                .phone("13308375411")
                .email("123@twu.com")
                .voteNum(10)
                .build();
        userRepository.save(user);

        RsEventEntity rsEvent = RsEventEntity.builder()
                .eventName("event 0")
                .keyword("key")
                .userId(user.getId())
                .build();
        rsEventRepository.save(rsEvent);

        mockMvc.perform(get("/rs/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName",is("event 0")))
                .andExpect(jsonPath("$.userDto.name",is("Tadashi")));

    }

//    @Test
//    void should_get_rs_list() throws Exception {
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
//
//    }
//
//    @Test
//    void should_get_one_rs_event() throws Exception {
//        mockMvc.perform(get("/rs/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第一条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")));
//
//        mockMvc.perform(get("/rs/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第二条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")));
//
//        mockMvc.perform(get("/rs/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.eventName", is("第三条事件")))
//                .andExpect(jsonPath("$.keyWord", is("无分类")));
//    }
//
//    @Test
//    void should_get_error_by_error_index() throws Exception {
//        mockMvc.perform(get("/rs/5"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid index")));
//    }
//
//    @Test
//    void should_get_rs_by_range() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")));
//    }
//
//    @Test
//    void should_get_error_by_error_range() throws Exception {
//        mockMvc.perform(get("/rs/list?start=1&end=4"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid request param")));
//    }
//
//    @Test
//    void should_add_a_rs_event_if_userName_is_exist() throws Exception {
//        UserDto userDto = new UserDto("wang", "male", 20, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("index","3"));
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[3].keyWord", is("经济")))
//                .andExpect(jsonPath("$[3].userDto.name", is("wang")));
//        mockMvc.perform(get("/user/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].name", is("zhang")))
//                .andExpect(jsonPath("$[1].name", is("wang")))
//                .andExpect(jsonPath("$[2].name", is("li")));
//    }
//
//    @Test
//    void should_add_a_rs_event_if_userName_is_not_exist_and_add_user_to_userDto() throws Exception {
//        UserDto userDto = new UserDto("zhao", "male", 20, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[3].keyWord", is("经济")))
//                .andExpect(jsonPath("$[3].userDto.name", is("zhao")));
//        mockMvc.perform(get("/user/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].name", is("zhang")))
//                .andExpect(jsonPath("$[1].name", is("wang")))
//                .andExpect(jsonPath("$[2].name", is("li")))
//                .andExpect(jsonPath("$[3].name", is("zhao")));
//
//    }
//
//    @Test
//    void should_not_add_a_rs_event_if_eventName_is_empty() throws Exception {
//        UserDto userDto = new UserDto("zhang", "male", 21, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid param")));
//    }
//
//    @Test
//    void should_not_add_a_rs_event_if_keyWord_is_empty() throws Exception {
//        UserDto userDto = new UserDto("zhang", "male", 21, "wenchang.li@twuc.com", "13308111111");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_a_rs_event_if_user_is_null() throws Exception {
//        RsEvent rsEvent = new RsEvent("", "经济", null);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_add_a_rs_event_if_user_is_empty() throws Exception {
//        UserDto userDto = new UserDto();
//        RsEvent rsEvent = new RsEvent("", "经济", userDto);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(rsEvent);
//
//        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
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
//
//    @Test
//    void remove_the_index_RsEvent_if_index_exist() throws Exception {
//
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMvc.perform(delete("/rs/event/2"))
//                .andExpect(status().isOk());
//        mockMvc.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")));
//    }

}
