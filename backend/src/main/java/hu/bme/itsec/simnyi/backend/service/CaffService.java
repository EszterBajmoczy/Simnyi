package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.model.Comment;
import hu.bme.itsec.simnyi.backend.model.dto.CaffDTO;
import hu.bme.itsec.simnyi.backend.model.dto.CaffUpdateDTO;
import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.repository.CaffRepository;
import hu.bme.itsec.simnyi.backend.repository.CommentRepository;
import hu.bme.itsec.simnyi.backend.repository.FileContentStore;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CaffService {

    private static final Logger logger = LogManager.getLogger(CaffService.class.getSimpleName());

    private final FileContentStore fileContentStore;

    private final CaffRepository caffRepository;

    private final CommentRepository commentRepository;

    public CaffService(FileContentStore fileContentStore, CaffRepository caffRepository, CommentRepository commentRepository) {
        this.fileContentStore = fileContentStore;
        this.caffRepository = caffRepository;
        this.commentRepository = commentRepository;
    }

    public void create(@NotNull String name, MultipartFile multipartFile) {
        var caff = new Caff();
        caff.setName(name);
        caff.setMimeType("CAFF");
        caff.setContentLength(multipartFile.getSize());
        try {
            caff.setContentId(StringUtils.isBlank(caff.getContentId()) ? name : caff.getContentId());
            var c = caffRepository.save(caff);
            c.setName(c.getId());
            caff = fileContentStore.setContent(c, multipartFile.getResource().getInputStream());
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath().substring(1);
            var bmpFileFullPath = savedCaffFileFullPath.substring(0, savedCaffFileFullPath.indexOf("."));
            var processBuilder = new ProcessBuilder(
                    "parser.exe",
                    savedCaffFileFullPath,
                    bmpFileFullPath);
            processBuilder.directory(new File("."));
            var process = processBuilder.start();
            process.waitFor(10, TimeUnit.SECONDS);
        } catch (IOException | InterruptedException e) {
            logger.error(e);
            throw new CustomHttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by uploading caff: " + e.getMessage());
        }
    }

    public void modify(@NotNull CaffUpdateDTO dto) {
        var caff = caffRepository.findById(dto.getId()).orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + dto.getId()));

        if(!caff.getName().equals(dto.getName())){
            caff.setName(dto.getName());
        }

        if(dto.getComment().size() != caff.getComment().size()){
            //at modify there is no option to add a new comment, just to remove it!
            var commentList = new ArrayList<Comment>();
            for(var c : caff.getComment()){
                if(dto.getComment().stream().anyMatch(r -> r.getId().equals(c.getId()))){
                    commentList.add(c);
                } else {
                    commentRepository.delete(c);
                }
            }
            caff.setComment(commentList);
        }

        caffRepository.save(caff);
    }

    public String findCaffById(@NotBlank String caffId) {
        var caff = caffRepository.findById(caffId).orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + caffId));
        var name = caff.getName();
        caff.setName(caff.getId());
        try {
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath().substring(1);
            var path = Paths.get(savedCaffFileFullPath);
            var data = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            logger.error(e);
            throw new CustomHttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by getting caff with id: " + caffId + " msg: " + e.getMessage());
        }
    }

    public CaffDTO getBmpByCaffId(@NotBlank String caffId) {
        var caff = caffRepository.findById(caffId).orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + caffId));
        return getBmpByCaff(caff);
    }

    public CaffDTO getBmpByCaff(Caff caff) {
        try{
            var name = caff.getName();
            caff.setName(caff.getId());
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath().substring(1);
            var bmpFileFullPath = savedCaffFileFullPath.substring(0, savedCaffFileFullPath.indexOf(".")) + ".bmp";
            var path = Paths.get(bmpFileFullPath);
            byte[] data = Files.readAllBytes(path);
            var content = Base64.getEncoder().encodeToString(data);
            var result = new CaffDTO();

            var cList = new ArrayList<CommentDTO>();
            for(var c : caff.getComment()){
                var comment = new CommentDTO();
                comment.setCaffId(c.getCaffId());
                comment.setContent(c.getContent());
                comment.setNameOfUser(c.getNameOfUser());
                comment.setId(c.getId());
                cList.add(comment);
            }
            result.setComment(cList);
            result.setName(name);
            result.setId(caff.getId());
            result.setContent(content);
            return result;
        } catch (Exception e){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "Error by getting bmp file with id: " + caff.getId());
        }
    }

    public List<CaffDTO> getBmps() {
        var caffList = caffRepository.findAll();
        var result = new ArrayList<CaffDTO>();

        for(var caff : caffList){
            var bmp = getBmpByCaff(caff);
            result.add(bmp);
        }
        return result;
    }

    public void delete(String caffId) {
        var caff = caffRepository.findById(caffId);
        if(caff != null){
            for(var c : caff.get().getComment()){
                commentRepository.delete(c);
            }
            caffRepository.deleteById(caffId);
        }
    }
}
