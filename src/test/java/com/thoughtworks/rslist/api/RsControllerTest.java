package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.request.RsEventPatchRequest;
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

        mockMvc.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].keyWord", is("key")))
                .andExpect(jsonPath("$[0].votNum", is(rsEvent.getVoteNum())))
                .andExpect(jsonPath("$[0].id", is(rsEvent.getId())))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].keyWord", is("key")))
                .andExpect(jsonPath("$[1].votNum", is(rsEvent1.getVoteNum())))
                .andExpect(jsonPath("$[1].id", is(rsEvent1.getId())));
    }

    @Test
    void should_get_rs_list_by_range() throws Exception {

        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        UserEntity user1 = saveOneUserEntity("lee", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent1 = saveOneRsEventEntity("event 1", "key", user1);

        mockMvc.perform(get("/rsEvents?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("event 0")))
                .andExpect(jsonPath("$[0].keyWord", is("key")))
                .andExpect(jsonPath("$[0].votNum", is(rsEvent.getVoteNum())))
                .andExpect(jsonPath("$[0].id", is(rsEvent.getId())))
                .andExpect(jsonPath("$[1].eventName", is("event 1")))
                .andExpect(jsonPath("$[1].keyWord", is("key")))
                .andExpect(jsonPath("$[1].votNum", is(rsEvent1.getVoteNum())))
                .andExpect(jsonPath("$[1].id", is(rsEvent1.getId())));
    }

    @Test
    void should_get_error_by_error_range() throws Exception {
        mockMvc.perform(get("/rsEvents?start=1&end=4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_get_one_event() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);
        mockMvc.perform(get("/rsEvent/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("key")))
                .andExpect(jsonPath("$.votNum", is(rsEvent.getVoteNum())))
                .andExpect(jsonPath("$.id", is(rsEvent.getId())));
    }

    @Test
    void should_get_error_by_error_userid() throws Exception {
        mockMvc.perform(get("/rsEvent/{id}", 5))
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

        mockMvc.perform(post("/rsEvent")
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

        mockMvc.perform(post("/rsEvent").content(json).contentType(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(post("/rsEvent")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void delet_rsEvent_by_id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        mockMvc.perform(delete("/rsEvent/{id}", rsEvent.getId()))
                .andExpect(status().isNoContent());
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0,rsEvents.size());
    }

    @Test
    public void should_update_rsEvent_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventPatchRequest rsEventPatchRequest = new RsEventPatchRequest("new event", "new key", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventPatchRequest);

        mockMvc.perform(patch("/rsEvent/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rsEvent/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("new event")))
                .andExpect(jsonPath("$.keyWord", is("new key")));

    }
    @Test
    public void should_just_update_rsEvent_keyWord_if_rsEvent_name_is_null_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventPatchRequest rsEventPatchRequest = new RsEventPatchRequest(null, "new key", user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventPatchRequest);

        mockMvc.perform(patch("/rsEvent/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rsEvent/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("event 0")))
                .andExpect(jsonPath("$.keyWord", is("new key")));

    }

    @Test
    public void should_just_update_rsEvent_name_if_keyWord_is_null_by_Id() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);

        RsEventPatchRequest rsEventPatchRequest = new RsEventPatchRequest("new event", null, user.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEventPatchRequest);

        mockMvc.perform(patch("/rsEvent/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rsEvent/{id}", rsEvent.getId()))
                .andExpect(jsonPath("$.eventName", is("new event")))
                .andExpect(jsonPath("$.keyWord", is("key")));

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

}
