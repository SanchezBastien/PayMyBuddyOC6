package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        SecurityConfigTest.FakeController.class,
        SecurityConfigTest.FakePublicController.class
})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private com.projet6.PayMyBuddy.Services.UserService userService;

    // Contrôleur simulé pour endpoint protégé
    @Controller
    static class FakeController {
        @GetMapping("/profile")
        @ResponseBody
        public String profile() {
            return "profile";
        }
    }

    // Contrôleur simulé pour endpoints publics
    @Controller
    static class FakePublicController {
        // /login doit retourner le nom d'une vue, sinon Spring Security lève 404
        @GetMapping("/login")
        public String loginPage() {
            return "login";
        }

        @GetMapping({"/signup", "/css/test.css", "/js/test.js"})
        @ResponseBody
        public String fake() {
            return "ok";
        }
    }

    @Test
    void passwordEncoderBeanShouldBeBCrypt() {
        String raw = "test123";
        String encoded = passwordEncoder.encode(raw);
        assertThat(passwordEncoder.matches(raw, encoded)).isTrue();
    }

    @Test
    void protectedEndpointRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
