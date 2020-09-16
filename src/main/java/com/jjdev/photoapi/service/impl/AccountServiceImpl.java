package com.jjdev.photoapi.service.impl;

import com.jjdev.photoapi.EmailConstructor;
import com.jjdev.photoapi.model.Role;
import com.jjdev.photoapi.model.User;
import com.jjdev.photoapi.model.UserRole;
import com.jjdev.photoapi.repository.RoleRepository;
import com.jjdev.photoapi.repository.UserRepository;
import com.jjdev.photoapi.service.AccountService;
import com.jjdev.photoapi.utility.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailConstructor emailConstructor;
    private JavaMailSender mailSender;

    @Autowired
    public AccountServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository,
            RoleRepository roleRepository, EmailConstructor emailConstructor,
            JavaMailSender mailSender) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailConstructor = emailConstructor;
        this.mailSender = mailSender;
    }

    @Override
    @Transactional
    public User saveUser(String name, String username, String email) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(UserRole.builder().user(user).role(this.findUserRoleByName("USER")).build());
        user.setUserRoles(userRoles);
        userRepository.save(user);
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Constants.TEMP_USER.toPath());
            String fileName = user.getId() + ".png";
            Path path = Paths.get(Constants.USER_FOLDER + fileName);
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO - Temporary log to avoid sending emails
        //mailSender.send(emailConstructor.constructNewUserEmail(user, password));
        System.out.println("Generated password: " + password);
        return user;
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {
        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, newPassword));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public Role findUserRoleByName(String role) {
        return roleRepository.findRoleByName(role);
    }

    @Override
    public User simpleSaveUser(User user) {
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
        return user;
    }

    @Override
    public User updateUser(User user, HashMap<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String bio = request.get("bio");
        user.setName(name);
        user.setEmail(email);
        user.setBio(bio);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructUpdateUserProfileEmail(user));
        return user;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void resetPassword(User user) {
        String password = RandomStringUtils.randomAlphanumeric(10);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));
    }

    @Override
    public List<User> getUserListByUsername(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
        byte[] bytes;
        try {
            Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png"));
            bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
            Files.write(path, bytes);
            return "User picture saved to server";
        } catch (Exception e) {
            return "Can't save user picture";
        }
    }
}
