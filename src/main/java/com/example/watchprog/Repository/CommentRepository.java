package com.example.watchprog.Repository;

import com.example.watchprog.Entity.Comment;
import com.example.watchprog.Entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Posts post);
    Comment findByIdAndUserId(Long commentId, Long userId);
}
