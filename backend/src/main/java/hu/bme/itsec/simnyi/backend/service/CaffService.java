package hu.bme.itsec.simnyi.backend.service;

import hu.bme.itsec.simnyi.backend.exception.CustomHttpException;
import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.repository.CaffRepository;
import hu.bme.itsec.simnyi.backend.repository.FileContentStore;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
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
            caff = fileContentStore.setContent(caff, multipartFile.getResource().getInputStream());
            caff.setContentId(StringUtils.isBlank(caff.getContentId()) ? name : caff.getContentId());
            caffRepository.save(caff);
            var savedCaffFileFullPath = fileContentStore.getResource(caff).getURI().getPath();
            var bmpFileFullPath = savedCaffFileFullPath.substring(0, savedCaffFileFullPath.indexOf(".")) + ".bmp";
//            var processBuilder = new ProcessBuilder(
//                    "native.exe",
//                    fileContentStore.getResource(caff).getURI().getPath(),
//                    bmpFileFullPath);
//            processBuilder.directory(new File("."));
//            var process = processBuilder.start();
//            process.waitFor(5, TimeUnit.SECONDS);
            // FIXME parser call rossz a exe, nem fut, nemtudom helpme

        } catch (IOException e) {
            logger.error(e);
            throw new CustomHttpException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public Caff findCaffById(@NotBlank String caffId) {
        var result = caffRepository.findCaffByName(caffId);
        return result.orElseThrow(() -> new CustomHttpException(HttpStatus.NOT_FOUND, "Caff not found with id: " + caffId));
    }

    private byte[] readBMP(String name) throws IOException {
        return IOUtils.toByteArray(resourceLoader.getResource(String.format("classpath:files/%s", name + "bmp")).getInputStream());
    }

}
