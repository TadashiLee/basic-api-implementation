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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Vote vote = new Vote(user.getId(), time, voteNum, rsEvent.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/rsEvent/{id}/vote", rsEvent.getId())
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<VoteEntity> votes = voteRepository.findAll();
        assertEquals(1, votes.size());
        assertEquals("event 0", votes.get(0).getRsEvents().getEventName());
        assertEquals(user.getId(), votes.get(0).getUser().getId());
        assertEquals(voteNum, votes.get(0).getNum());
    }


//    @Test
//    public void get_votes_by_userId_and_rsEventId() throws Exception {
//        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
//        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);
//        VoteEntity vote = saveOneVoteEntity(5, new Timestamp(System.currentTimeMillis()), rsEvent, user);
//        VoteEntity vote1 = saveOneVoteEntity(1, new Timestamp(System.currentTimeMillis()), rsEvent, user);
//        VoteEntity vote2 = saveOneVoteEntity(2, new Timestamp(System.currentTimeMillis()), rsEvent, user);
//        VoteEntity vote3 = saveOneVoteEntity(3, new Timestamp(System.currentTimeMillis()), rsEvent, user);
//        VoteEntity vote4 = saveOneVoteEntity(4, new Timestamp(System.currentTimeMillis()), rsEvent, user);
//
//        mockMvc.perform(get("/votes")
//                .param("userId", String.valueOf(user.getId()))
//                .param("rsEventId", String.valueOf(rsEvent.getId())))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(5)))
//                .andExpect(jsonPath("$[0].userId", is(user.getId())))
//                .andExpect(jsonPath("$[0].rsEventId", is(rsEvent.getId())))
//                .andExpect(jsonPath("$[0].voteNum", is(5)));
//    }

    @Test
    public void get_votes_by_time_range() throws Exception {
        UserEntity user = saveOneUserEntity("Tadashi", "male", 20, "13308375411", "123@twu.com", 10);
        RsEventEntity rsEvent = saveOneRsEventEntity("event 0", "key", user);
        VoteEntity vote = saveOneVoteEntity(5, new Timestamp(System.currentTimeMillis()), rsEvent, user);
        VoteEntity vote1 = saveOneVoteEntity(1, new Timestamp(System.currentTimeMillis()), rsEvent, user);
        VoteEntity vote2 = saveOneVoteEntity(2, new Timestamp(System.currentTimeMillis()), rsEvent, user);
        VoteEntity vote3 = saveOneVoteEntity(3, new Timestamp(System.currentTimeMillis()), rsEvent, user);
        VoteEntity vote4 = saveOneVoteEntity(4, new Timestamp(System.currentTimeMillis()), rsEvent, user);

        mockMvc.perform(get("/votes")
                .param("start", String.valueOf(vote.getTime()))
                .param("end", String.valueOf(vote4.getTime())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(user.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEvent.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(5)));
    }

    private VoteEntity saveOneVoteEntity(int voteNum, Timestamp time, RsEventEntity rsEvent, UserEntity user) {
        VoteEntity vote = VoteEntity.builder()
                .num(voteNum)
                .time(time)
                .rsEvents(rsEvent)
                .user(user)
                .build();
        voteRepository.save(vote);
        return vote;
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
