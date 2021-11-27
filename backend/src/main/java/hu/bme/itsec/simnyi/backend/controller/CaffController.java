package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.service.CaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@RestController
public class CaffController {


    private final CaffService caffService;

    @Autowired
    public CaffController(CaffService caffService) {
        this.caffService = caffService;
    }

    @PostMapping(path = "/caff", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@Validated @NonNull @RequestParam(name = "name") String name,
                                       @Validated @NonNull @RequestBody MultipartFile file) {
        caffService.create(name, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/caff/{caffId}")
    public ResponseEntity<Caff> findCaffById(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        return ResponseEntity.ok(caffService.findCaffById(caffId));
    }
}
