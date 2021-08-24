package com.example.watchprog.Services;

import com.example.watchprog.DTO.PostDTO;
import com.example.watchprog.Entity.ImageModel;
import com.example.watchprog.Entity.Posts;
import com.example.watchprog.Entity.User;
import com.example.watchprog.Exception.PostsNotFoundException;
import com.example.watchprog.Repository.CommentRepository;
import com.example.watchprog.Repository.ImageRepository;
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
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostsRepository postsRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Posts createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Posts post = new Posts();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving post for Users: {}", user.getEmail());
        return postsRepository.save(post);
    }

    public List<Posts> getAllPosts() {
        return postsRepository.findAllByOrderByCreateDateDesc();
    }

    public Posts getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postsRepository.findPostsByIdAndUser(postId, user)
                .orElseThrow(() -> new PostsNotFoundException("Post can't be found for username " + user.getEmail()));

    }

    public List<Posts> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postsRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public Posts likePost(Long postId, String username) {

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostsNotFoundException("Post can't be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream().filter(u -> u.equals(username)).findAny();

        if(userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postsRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {

        Posts post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postsRepository.delete(post);
        imageModel.ifPresent(imageRepository ::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username - " + username + " not found"));
    }
}
