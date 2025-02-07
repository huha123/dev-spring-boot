package dev.huha123.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class PasswordEncderTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test1() {
        String password = "1";
        String password1 = "1";
        String encode = passwordEncoder.encode(password);
        log.info("## ENCODE :{}", encode);
        log.info("### passwordEncoder.matches(password, encode): {}", passwordEncoder.matches(password1, encode));
    }
}
