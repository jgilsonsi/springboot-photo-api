package com.jjdev.photoapi.service;

import com.jjdev.photoapi.model.Post;

public interface CommentService {

    public void saveComment(Post post, String username, String content);
}
