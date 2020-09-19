package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteConrollerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();
    }

    @Test
    public void should_add_a_vote_by_eventId() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);
        int voteNum = 5;
        long timeLong = 1473247063900L;
        Timestamp time = new Timestamp(timeLong);
        Vote vote = new Vote(user.getId(), time, voteNum);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/vote/event/{id}", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<VoteEntity> votes = voteRepository.findAll();
        assertEquals(1, votes.size());
        assertEquals("event 0", votes.get(0).getRsEvents().getEventName());
        assertEquals(user.getId(), votes.get(0).getUser().getId());
        assertEquals(voteNum, votes.get(0).getNum());
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
