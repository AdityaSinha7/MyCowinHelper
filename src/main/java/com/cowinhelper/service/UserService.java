package com.cowinhelper.service;

import com.cowinhelper.entity.User;
import com.cowinhelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(Long userId,String firstName,String userName){
        User newUser = new User();
        newUser.setId(userId);
        newUser.setFirstName(firstName);
        newUser.setUserName(userName);
        return userRepository.save(newUser);
    }

    public void deleteUser(User user){
        if(userRepository.existsById(user.getId())){
            userRepository.delete(user);
        }
    }

    public User updatePincode(User user,String pincode){
        user.setPincode(pincode);
        return userRepository.save(user);
    }

    public User updateAge(User user,int age){
        user.setAge(age);
        return userRepository.save(user);
    }

    public Optional<User> findUser(Long userId){
        return userRepository.findById(userId);
    }

}
