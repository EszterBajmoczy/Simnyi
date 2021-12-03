package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {


    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/comment")
    public ResponseEntity<Void> upload(@Validated @NonNull @RequestBody CommentDTO comment) {
        if(comment.getCaffId() == null || comment.getContent() == null){
            return ResponseEntity.badRequest().build();
        }
        commentService.createComment(comment);
        return ResponseEntity.ok().build();
    }
}
