package com.jjdev.photoapi.repository;

import com.jjdev.photoapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p ORDER BY p.postedDate DESC")
    public List<Post> findAll();

    @Query("SELECT p FROM Post p WHERE p.username=:username ORDER BY p.postedDate DESC")
    public List<Post> findPostByUsername(@Param("username") String username);

    @Query("SELECT p FROM Post p WHERE p.id=:id")
    public Post findPostById(@Param("id") Long id);

    @Modifying
    @Query("DELETE Post WHERE id=:id")
    public void deletePostById(@Param("id") Long id);
}
