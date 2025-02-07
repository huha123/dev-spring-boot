package dev.huha123.app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.huha123.app.entity.Member;
import dev.huha123.app.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class JpaTests {

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    class Auth_Test {
        @Test
        @DisplayName(value = "auth find email and password")
        void auth_findByEmailAndPassword() throws Exception {
            // INSERT INTO `member` VALUES (1, 'admin', 'admin@gmail.com', '{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be', '01099998888', now(), null);
            String email = "admin@gmail.com";
            String password = "{bcrypt}$2a$10$oDTdFtjc8EngFXk8zBrR4ecO17/PDz60C8XtkVczmfI.wN57e21Be";   //1
            Member member = memberRepository.findByEmailAndPassword(email, password).get();
            log.info("### auth_findByEmailAndPassword :{}", member);
        }
    }
}
