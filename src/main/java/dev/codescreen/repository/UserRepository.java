package dev.codescreen.repository;

import dev.codescreen.exception.UserNotFoundException;
import dev.codescreen.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//Class to define the preset of all users registered to the transaction service
@Repository
public class UserRepository {
    private final List<User> userList;

    public UserRepository(){
        this.userList = new ArrayList<>();
        userList.add(new User("Person A", "1", 0.00));
        userList.add(new User("Person B", "2", 0.00));
        userList.add(new User("Person C", "3", 0.00));
        userList.add(new User("Person D", "4", 350.00));
    }

    public List<User> getAllUsers(){
        return userList;
    }

    public User findUserById(String id){
        for (User user : userList) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new UserNotFoundException("User not found with ID: " + id);
    }

}
