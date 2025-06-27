package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.AddFriendController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AddFriendController.class)
public class AddFriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ConnectionService connectionService;
    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    public void testGetEndpoint() throws Exception {
        mockMvc.perform(get("/addfriend"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostEndpoint() throws Exception {
        Mockito.when(userService.getUserByEmail("value1")).thenReturn(Optional.of(new User()));
        Principal principal = ()-> "test1";
        mockMvc.perform(post("/addfriend")
                        .queryParam("friendEmail", "value1").principal(principal))
                .andExpect(status().isOk());
    }
}


