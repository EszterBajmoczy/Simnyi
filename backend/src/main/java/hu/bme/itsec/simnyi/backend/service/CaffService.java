package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.model.Comment;
import hu.bme.itsec.simnyi.backend.model.dto.CaffDTO;
import hu.bme.itsec.simnyi.backend.model.dto.CaffUpdateDTO;
import hu.bme.itsec.simnyi.backend.model.dto.CommentDTO;
import hu.bme.itsec.simnyi.backend.repository.CaffRepository;
import hu.bme.itsec.simnyi.backend.repository.FileContentStore;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CaffService {

    private static final Logger logger = LogManager.getLogger(CaffService.class.getSimpleName());

    private final FileContentStore fileContentStore;

    private final CaffRepository caffRepository;

    private final ResourceLoader resourceLoader;

    public CaffService(FileContentStore fileContentStore, CaffRepository caffRepository, ResourceLoader resourceLoader) {
        this.fileContentStore = fileContentStore;
        this.caffRepository = caffRepository;
        this.resourceLoader = resourceLoader;
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
                if(dto.getComment().contains(c)){
                    commentList.add(c);
                }
                //TODO Check if the comment will also delete, not just removed from the list
            }
            caff.setComment(commentList);
        }

        caffRepository.save(caff);
        //TODO modify bmp too!!!!
    }

    public MultipartFile findCaffById(@NotBlank String caffId) {
        var caff = caffRepository.findCaffByName(caffId).orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + caffId));
        var name = caff.getName();
        caff.setName(caff.getId());
        try {
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath().substring(1);
            var path = Paths.get(savedCaffFileFullPath);
            var data = Files.readAllBytes(path);
            return new MockMultipartFile(name, data);
        } catch (IOException e) {
            logger.error(e);
            throw new CustomHttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by getting caff with id: " + caffId + " msg: " + e.getMessage());
        }
    }

    public CaffDTO getBmpByCaffId(@NotBlank String caffId) {
        var caff = caffRepository.findCaffByName(caffId).orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + caffId));
        return getBmpByCaff(caff);
    }

    public CaffDTO getBmpByCaff(Caff caff) {
        try{
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath();
            var bmpFileFullPath = savedCaffFileFullPath.substring(0, savedCaffFileFullPath.indexOf(".")) + ".bmp";
            var path = Paths.get(bmpFileFullPath);
            byte[] data = Files.readAllBytes(path);

            var result = new CaffDTO();

            var cList = new ArrayList<CommentDTO>();
            for(var c : caff.getComment()){
                var comment = new CommentDTO();
                comment.setCaffId(c.getCaff().getId());
                comment.setContent(c.getContent());
                comment.setNameOfUser(c.getNameOfUser().getUsername());
                comment.setId(c.getId());
                cList.add(comment);
            }
            result.setComment(cList);
            result.setName(caff.getName());
            result.setId(caff.getId());
            result.setContent(data);
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

    private byte[] readBMP(String name) throws IOException {
        return IOUtils.toByteArray(resourceLoader.getResource(String.format("classpath:files/%s", name + "bmp")).getInputStream());
    }

    public CaffDTO getTmpBmpByCaffId(@NotBlank String caffId) {
        var path = Paths.get("src/main/resources/files/test.bmp");
        try{
            byte[] data = Files.readAllBytes(path);

            var dto = new CaffDTO();
            dto.setId(caffId);
            dto.setContent(data);
            dto.setName(caffId);
            return dto;
        } catch (Exception e){
            throw new CustomHttpException(HttpStatus.BAD_REQUEST, "something wrong");
        }
    }

    public List<CaffDTO> getTmpBmps(){
        var list = new ArrayList<CaffDTO>();
        list.add(getTmpBmpByCaffId("Test1111"));
        list.add(getTmpBmpByCaffId("Test324"));
        list.add(getTmpBmpByCaffId("ewrt"));
        list.add(getTmpBmpByCaffId("alma"));
        list.add(getTmpBmpByCaffId("Test111ghjg1"));
        list.add(getTmpBmpByCaffId("sgfh"));
        return list;
    }
}
