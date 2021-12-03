package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Comment;
import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.repository.CaffRepository;
import hu.bme.itsec.simnyi.backend.repository.CommentRepository;
import hu.bme.itsec.simnyi.backend.repository.UserRepository;
import hu.bme.itsec.simnyi.backend.security.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class CommentService {

    private final JwtTokenUtil jwtTokenUtil;

    private final CaffRepository caffRepository;

    private final CommentRepository commentRepository;

    public CommentService(CaffRepository caffRepository, CommentRepository commentRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.caffRepository = caffRepository;
        this.commentRepository = commentRepository;
    }

    public void createComment(@NotNull CommentDTO dto){
        var caff = caffRepository.findById(dto.getCaffId());
        var signedInUserName = jwtTokenUtil.getCurrentUsername();

        if(dto.getNameOfUser() != null && !dto.getNameOfUser().isBlank() && !signedInUserName.equals(dto.getNameOfUser())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Username on the comment does not match with the signed in user's name.");
        }

        if(caff.isPresent()){
            var comment = new Comment();
            comment.setNameOfUser(signedInUserName);
            comment.setContent(dto.getContent());
            comment.setCaffId(caff.get().getId());
            var c = commentRepository.save(comment);
            caff.get().getComment().add(c);
            caffRepository.save(caff.get());
        } else {
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Caff not found with id: " + dto.getCaffId());
        }
    }
}
