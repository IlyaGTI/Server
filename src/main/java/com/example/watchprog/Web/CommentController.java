package com.example.watchprog.Web;

import com.example.watchprog.DTO.CommentDTO;
import com.example.watchprog.Entity.Comment;
import com.example.watchprog.Facade.CommentFacade;
import com.example.watchprog.PayLoad.Response.MessageResponse;
import com.example.watchprog.Services.CommentService;
import com.example.watchprog.Validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    private CommentService commentService;
    private CommentFacade commentFacade;
    private ResponseErrorValidation responseErrorValidation;


    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);

        CommentDTO createComment = commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(createComment, HttpStatus.OK);
    }


    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable("postId") String postId) {
        List<CommentDTO> comment = commentService.getAllCommentsForPosts(Long.parseLong(postId)).stream()
                          .map(commentFacade::commentToCommentDTO)
                           .collect(Collectors.toList());

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comments was deleted"), HttpStatus.OK);
    }
}
