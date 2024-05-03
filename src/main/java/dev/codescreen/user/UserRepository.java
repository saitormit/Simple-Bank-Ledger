package dev.codescreen.user;

import org.springframework.stereotype.Repository;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private List<User> userList = new ArrayList<>();

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
        return null;
    }

}
