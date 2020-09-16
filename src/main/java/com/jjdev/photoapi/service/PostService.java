package com.jjdev.photoapi.service;

import com.jjdev.photoapi.model.Post;
import com.jjdev.photoapi.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface PostService {

    public Post savePost(User user, HashMap<String, String> request, String postImageName);

    public List<Post> postList();

    public Post getPostById(Long id);

    public List<Post> findPostByUsername(String username);

    public Post deletePost(Post post);

    public String savePostImage(MultipartFile multipartFile, String fileName);
}
