package dev.huha123.app;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.huha123.app.entity.Board;
import dev.huha123.app.entity.Member;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class ApiTests {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    private String token;

    @BeforeEach
    void init() throws Exception {
        String content = mapper.writeValueAsString(Map.ofEntries(
                Map.entry("email", "admin@gmail.com"),
                Map.entry("password", "1")));
        log.info("### content :{}", content);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        log.info("## result: {}", result.getResponse().getHeaderNames());
        token = result.getResponse().getHeader("Authorization");
        log.info("## token :{}", token);
    }

    @Test
    void test1() {
        /* init start test */
    }

    @Nested
    class Board_Test {
        @Test
        @DisplayName(value = "board list test")
        void board_list() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/board")
                    .header("Authorization", token))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName(value = "board insert test")
        void board_insert() throws JsonProcessingException, Exception {
            Board board = new Board();
            board.setTitle("insert title 한글");
            board.setContent("insert content 한글??");
            board.setPassword("1234");

            mockMvc.perform(MockMvcRequestBuilders.post("/board")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(board)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName(value = "board update test")
        void board_update() throws JsonProcessingException, Exception {
            Board board = new Board();
            board.setId(Long.valueOf(1));
            board.setTitle("update title");
            board.setContent("update content");
            board.setPassword("1234");

            mockMvc.perform(MockMvcRequestBuilders.put("/board")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(board)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName(value = "board delete test")
        void board_delete() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/board/2")
                    .header("Authorization", token))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

    }

    @Nested
    class Member_Test {
        @Test
        @DisplayName(value = "member list test")
        void member_list() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName(value = "member insert test")
        void member_insert() throws JsonProcessingException, Exception {
            Member member = new Member();
            member.setEmail(UUID.randomUUID().toString().concat("@test.com"));
            member.setPassword("1");
            member.setName("insert name");

            Random random = new Random();
            member.setPhoneNumber("010".concat(String.valueOf(random.nextInt(99999999))));

            mockMvc.perform(MockMvcRequestBuilders.post("/member")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(member)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName(value = "member delete test")
        void member_delete() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.delete("/member/2"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class Auth_test {
        @Test
        void roles_list() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/role"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        void privileges_list() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/auth"))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}
