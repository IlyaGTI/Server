package com.example.watchprog.Web;

import com.example.watchprog.DTO.PostDTO;
import com.example.watchprog.Entity.Posts;
import com.example.watchprog.Facade.PostFacade;
import com.example.watchprog.PayLoad.Response.MessageResponse;
import com.example.watchprog.Services.PostService;
import com.example.watchprog.Validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {
    private PostFacade postFacade;
    private PostService postService;
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PostController(PostFacade postFacade, PostService postService, ResponseErrorValidation responseErrorValidation) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult,
                                             Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (ObjectUtils.isEmpty(errors)) return errors;

        Posts post = postService.createPost(postDTO, principal);
        PostDTO createdPost = postFacade.postsToPostDTO(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);


    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts().
                stream().map(postFacade::postsToPostDTO).collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);

    }
    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostForUser(principal)
                .stream().map(postFacade::postsToPostDTO).collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") String postId,
                                            @PathVariable("username") String username) {
        Posts post = postService.likePost(Long.parseLong(postId), username);
        PostDTO postDTO = postFacade.postsToPostDTO(post);
        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("Post was delete"), HttpStatus.OK);
    }
}
