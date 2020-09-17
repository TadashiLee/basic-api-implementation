package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    public void should_register_user() throws Exception {
        UserDto userDto = new UserDto("Tadashi","male",20,"wenchang.li@twuc.com","13308111111");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1,users.size());
        assertEquals("Tadashi", users.get(0).getUserName());

    }

    @Test
    public void get_user_by_id() throws Exception {
        UserDto userDto = new UserDto("Tadashi","male",20,"wenchang.li@twuc.com","13308111111");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users = userRepository.findAll();
        assertEquals(1,users.size());
        assertEquals("Tadashi", users.get(0).getUserName());

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tadashi")));

    }

    @Test
    public void delet_user_by_id() throws Exception {
        UserDto userDto = new UserDto("Tadashi","male",20,"wenchang.li@twuc.com","13308111111");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/user/event/1"))
                .andExpect(status().isOk());
        List<UserEntity> users = userRepository.findAll();
        assertEquals(0,users.size());

    }
//    @Test
//    void should_get_userDto() throws Exception {
//        mockMvc.perform(get("/user/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].name", is("zhang")))
//                .andExpect(jsonPath("$[1].name", is("wang")))
//                .andExpect(jsonPath("$[2].name", is("li")));
//
//    }
//
//    @Test
//    void should_get_one_userDto() throws Exception {
//        mockMvc.perform(get("/user/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("zhang")));
//
//        mockMvc.perform(get("/user/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("wang")));
//
//        mockMvc.perform(get("/user/3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is("li")));
//    }
//
//    @Test
//    void should_get_rs_by_range() throws Exception {
//        mockMvc.perform(get("/user/list?start=1&end=3"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].name", is("zhang")))
//                .andExpect(jsonPath("$[1].name", is("wang")))
//                .andExpect(jsonPath("$[2].name", is("li")));
//    }
//
//    @Test
//    void should_register_user() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",20,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void should_not_register_name_empty() throws Exception {
//        UserDto userDto = new UserDto("","male",20,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("invalid user")));
//    }
//
//    @Test
//    void should_not_register_name_is_more_than_8() throws Exception {
//        UserDto userDto = new UserDto("123456789","male",20,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_gender_empty() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","",20,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_age_null() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",null,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_age_is_lower_than_18() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",17,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_age_is_larger_than_100() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",101,"wenchang.li@twuc.com","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_email_is_empty() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",18,"","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_email_is_invalidate() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",18,"@","13308111111");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_phone_is_invalidate_begin_with_no_1() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",18,"wenchang.li@twuc.com","20123456789");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_phone_is_invalidate_to_short() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",18,"wenchang.li@twuc.com","20123456789");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void should_not_register_phone_is_empty() throws Exception {
//        UserDto userDto = new UserDto("Tadashi","male",18,"wenchang.li@twuc.com","");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String userDtoJson = objectMapper.writeValueAsString(userDto);
//
//        mockMvc.perform(post("/user/register").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}
