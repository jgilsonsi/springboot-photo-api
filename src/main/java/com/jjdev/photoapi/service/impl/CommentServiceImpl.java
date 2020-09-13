package com.jjdev.photoapi.service.impl;

import com.jjdev.photoapi.model.Comment;
import com.jjdev.photoapi.model.Post;
import com.jjdev.photoapi.repository.CommentRepository;
import com.jjdev.photoapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveComment(Post post, String username, String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUsername(username);
        comment.setPostedDate(new Date());
        post.setComments(comment);
        commentRepository.save(comment);
    }
}
