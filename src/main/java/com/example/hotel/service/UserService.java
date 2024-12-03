package com.example.hotel.service;

import com.example.hotel.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Integer id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registerUser(UserDTO userDTO) {
        return null;
    }

    public User changePassword(String email, String newPassword) {
        return null;
    }

    public Optional<User> findUserById(Integer id) {
        return null;
    }

    public Optional<User> findByEmail(String email) {
        return null;
    }
}
