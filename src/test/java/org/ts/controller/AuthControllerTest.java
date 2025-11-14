package org.ts.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ts.model.User;
import org.ts.service.AuthService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    private final InputStream originalIn = System.in;
    @Mock
    AuthService authService;
    @InjectMocks
    AuthController authController;

    @Test
    void testCheckLogin(){
        String loginInputs = "l\ntest.username\ntestP@ssw0rd\ne\n";
        User mockUser =  new User("random-uuid", "test.username", "test.user@gmail.com", "test.fullname", "someencryptedpassed");
        try {
            Mockito.when(authService.login("test.username", "testP@ssw0rd")).thenReturn(mockUser);
        } catch (Exception e) {
        }
        System.setIn(new ByteArrayInputStream(loginInputs.getBytes()));
        User user = authController.run();
        Assertions.assertEquals("test.fullname", user.getFullName());
    }

    @Test void testCheckSignUp(){
        String loginInputs = "s\ntest.username\ntest.user@gmail.com\ntest.fullname\ntestP@ssw0rd\ntestP@ssw0rd\ne\n";
        User mockUser =  new User("random-uuid", "test.username", "test.user@gmail.com", "test.fullname", "someencryptedpassed");
        try {
            Mockito.when(authService.signUp("test.username", "test.user@gmail.com", "test.fullname", "testP@ssw0rd")).thenReturn(mockUser);
        } catch (Exception e) {
        }
        System.setIn(new ByteArrayInputStream(loginInputs.getBytes()));
        User user = authController.run();
        Assertions.assertEquals("test.fullname", user.getFullName());
    }

    @AfterEach
    void tearDown(){
        System.setIn(originalIn);
    }
}
