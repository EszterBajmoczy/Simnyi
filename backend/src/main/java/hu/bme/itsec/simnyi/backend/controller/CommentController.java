package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.service.CaffService;
import hu.bme.itsec.simnyi.backend.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

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
        commentService.createComment(comment);
        return ResponseEntity.ok().build();
    }
}
