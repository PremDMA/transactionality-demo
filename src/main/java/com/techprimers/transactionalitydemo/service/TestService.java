package com.techprimers.transactionalitydemo.service;

import com.techprimers.transactionalitydemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TestService {
    @Autowired
    private UserService userService;

    @Transactional
    public void test() throws Exception {

        try {
            User user1 = new User("Peter", "Ops", 12000L);
            User user2 = new User("Sam", "Tech", 22000L);
            userService.insert(Arrays.asList(
                    user1, user2
            ));
        }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 1...!" + exception.getMessage());
        }


        try {
            User user4 = new User("Ryan King", "Tech", 32000L);
            User user3 = new User("Nick", "Ops", 18000L);
            userService.insert(Arrays.asList(
                    user3, user4
            ));
        }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 2...!" + exception.getMessage());
            System.out.println(userService.getUsers());
            throw exception;
        }
        
    }
}

