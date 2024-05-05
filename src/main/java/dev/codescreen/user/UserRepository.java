package dev.codescreen.user;

import dev.codescreen.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

//Class to define the preset of all users registered to the transaction service
@Repository
public class UserRepository {
    private final List<User> userList;

    public UserRepository(){
        this.userList = new ArrayList<>();
        userList.add(new User("Person A", "1", 2000.00));
        userList.add(new User("Person B", "2", 4000.00));
        userList.add(new User("Person C", "3", 1000.00));
        userList.add(new User("Person D", "4", 0.00));
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
