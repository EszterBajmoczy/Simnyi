package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Comment;
import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.repository.CaffRepository;
import hu.bme.itsec.simnyi.backend.repository.UserRepository;
import hu.bme.itsec.simnyi.backend.security.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class CommentService {

    private static final Logger logger = LogManager.getLogger(CommentService.class.getSimpleName());

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final CaffRepository caffRepository;

    public CommentService(CaffRepository caffRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.caffRepository = caffRepository;
    }

    public void createComment(@NotNull CommentDTO dto){
        var caff = caffRepository.findById(dto.getCaffId());
        var signedInUserName = jwtTokenUtil.getCurrentUsername();

        if(!dto.getNameOfUser().isBlank() && !signedInUserName.equals(dto.getNameOfUser())){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Username on the comment does not match with the signed in user's name.");
        }
        var user = userRepository.findUserByUsername(signedInUserName).get();

        if(caff.isPresent()){
            var comment = new Comment();
            comment.setNameOfUser(user);
            comment.setContent(dto.getContent());
            comment.setCaff(caff.get());
            caff.get().getComment().add(new Comment());
            caffRepository.save(caff.get());
        } else {
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Caff not found with id: " + dto.getCaffId());
        }
    }
}
