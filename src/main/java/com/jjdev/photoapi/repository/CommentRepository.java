package com.jjdev.photoapi.repository;

import com.jjdev.photoapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}