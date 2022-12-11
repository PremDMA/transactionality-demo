package com.techprimers.transactionalitydemo.service;

import com.techprimers.transactionalitydemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(List<User> users) {

        for (User user : users) {
            System.out.println("Inserting Data for User name: " + user.getName());
            jdbcTemplate.update("insert into USER(Name, Dept, Salary) values (?, ?, ?)",
                    preparedStatement -> {
                        preparedStatement.setString(1, user.getName());
                        preparedStatement.setString(2, user.getDept());
                        preparedStatement.setLong(3, user.getSalary());
                    });
        }
    }

    public List<User> getUsers() {
        System.out.println("Retrieve all Users List...");
        List<User> userList = jdbcTemplate.query("select Name, Dept, Salary from USER", (resultSet, i) -> new User(resultSet.getString("Name"),
                resultSet.getString("Dept"),
                resultSet.getLong("Salary")));

        return userList;
    }


    @Transactional
    public void test() throws Exception {

        try {
            User user1 = new User("Peter", "Ops", 12000L);
            User user2 = new User("Sam", "Tech", 22000L);
            insert(Arrays.asList(
                    user1, user2
            ));
        }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 1...!" + exception.getMessage());
        }


        try {
            User user4 = new User("Ryan King", "Tech", 32000L);
            User user3 = new User("Nick", "Ops", 18000L);
            insert(Arrays.asList(
                    user3, user4
            ));
        }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 2...!" + exception.getMessage());
            System.out.println(getUsers());
            throw exception;
        }
        
    }
}
