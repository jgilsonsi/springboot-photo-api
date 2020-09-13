package com.jjdev.photoapi.service;

import com.jjdev.photoapi.model.Role;
import com.jjdev.photoapi.model.User;
import org.springframework.web.multipart.MultipartFile;
import reactor.netty.http.server.HttpServerRequest;

import java.util.HashMap;
import java.util.List;

public interface AccountService {

    public User saveUser(String name, String username, String email);

    public User findByUsername(String username);

    public User findByEmail(String email);

    public List<User> userList();

    public Role findUserRoleByName(String role);

    public Role saveRole(Role role);

    public void updateUserPassword(User user, String newPassword);

    public User updateUser(User user, HashMap<String, String> request);

    public User findUserById(Long id);

    public void deleteUser(User user);

    public void resetPassword(User user);

    public List<User> getUserListByUsername(String username);

    public User simpleSaveUser(User user);

    public String saveUserImage(MultipartFile multipartFile, Long userImageId);
}