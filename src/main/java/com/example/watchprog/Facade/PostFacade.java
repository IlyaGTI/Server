package com.example.watchprog.Facade;

import com.example.watchprog.DTO.PostDTO;
import com.example.watchprog.Entity.Posts;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    public PostDTO postsToPostDTO(Posts post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setCaption(post.getCaption());
        postDTO.setLikes(post.getLikes());
        postDTO.setUsersLiked(post.getLikedUsers());
        postDTO.setLocation(post.getLocation());

        return postDTO;
    }
}
