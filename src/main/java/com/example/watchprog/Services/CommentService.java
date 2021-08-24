package com.example.watchprog.Services;

import com.example.watchprog.DTO.CommentDTO;
import com.example.watchprog.Entity.Comment;
import com.example.watchprog.Entity.Posts;
import com.example.watchprog.Entity.User;
import com.example.watchprog.Exception.PostsNotFoundException;
import com.example.watchprog.Repository.CommentRepository;
import com.example.watchprog.Repository.PostsRepository;
import com.example.watchprog.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostsRepository postsRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {

        User user = getUserByPrincipal(principal);
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->  new PostsNotFoundException("Post can't be found for username " + user.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());


        LOG.info("Saving comment for post : {}", post.getId());

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPosts(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() ->  new PostsNotFoundException("Post can't be found "));

        List<Comment> comments = commentRepository.findAllByPost(post);

        return comments;
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username - " + username + " not found"));
    }
}
