package com.jjdev.photoapi.service.impl;

import com.jjdev.photoapi.model.Post;
import com.jjdev.photoapi.model.User;
import com.jjdev.photoapi.repository.PostRepository;
import com.jjdev.photoapi.service.PostService;
import com.jjdev.photoapi.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post savePost(User user, HashMap<String, String> request, String postImageName) {
        String caption = request.get("caption");
        String location = request.get("location");
        Post post = Post.builder()
                .caption(caption)
                .location(location)
                .username(user.getUsername())
                .userImageId(user.getId())
                .build();
        user.setPost(post);
        postRepository.save(post);
        return post;
    }

    @Override
    public List<Post> postList() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findPostById(id);
    }

    @Override
    public List<Post> findPostByUsername(String username) {
        return postRepository.findPostByUsername(username);
    }

    @Override
    public Post deletePost(Post post) {
        try {
            Files.deleteIfExists(Paths.get(Constants.POST_FOLDER + "/" + post.getName() + ".png"));
            postRepository.deletePostById(post.getId());
            return post;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public String savePostImage(MultipartFile multipartFile, String fileName) {
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(Constants.POST_FOLDER + fileName + ".png");
            Files.write(path, bytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            System.out.println("Error occurred. Photo not saved!");
            return "Error occurred. Photo not saved!";
        }
        System.out.println("Photo saved successfully!");
        return "Photo saved successfully!";
    }
}
