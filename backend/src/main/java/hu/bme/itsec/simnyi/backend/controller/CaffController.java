package hu.bme.itsec.simnyi.backend.controller;

import hu.bme.itsec.simnyi.backend.model.Caff;
import hu.bme.itsec.simnyi.backend.model.dto.CaffDTO;
import hu.bme.itsec.simnyi.backend.model.dto.CaffUpdateDTO;
import hu.bme.itsec.simnyi.backend.service.CaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
public class CaffController {

    private final CaffService caffService;

    @Autowired
    public CaffController(CaffService caffService) {
        this.caffService = caffService;
    }

    @PostMapping(path = "public/caff", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@Validated @NonNull @RequestParam(name = "name") String name,
                                       @Validated @NonNull @RequestBody MultipartFile file) {
        caffService.create(name, file);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/caff")
    public ResponseEntity<Void> modify(@Validated @RequestBody CaffUpdateDTO dto) {
        caffService.modify(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/public/caff/{caffId}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> downloadCaffById(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        var multipartFile = caffService.findCaffById(caffId);
        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(multipartFile.getResource());
    }

    @GetMapping(path = "/public/caff/bmp/{caffId}")
    public ResponseEntity<CaffDTO> getBmpByCaffId(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        return ResponseEntity.ok(caffService.getBmpByCaffId(caffId));
    }

    @GetMapping(path = "/public/caff/bmp")
    public ResponseEntity<List<CaffDTO>> getAllBmp(){
        return ResponseEntity.ok(caffService.getBmps());
    }

    //tmp!!!
    @GetMapping(path = "/public/caff/tmp/bmp/{caffId}")
    public ResponseEntity<CaffDTO> getTmpBmpByCaffId(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        return ResponseEntity.ok(caffService.getTmpBmpByCaffId(caffId));
    }

    @GetMapping(path = "/public/caff/tmp/bmp")
    public ResponseEntity<List<CaffDTO>> getTmpAllBmp(){
        return ResponseEntity.ok(caffService.getTmpBmps());
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/caff/tmp/bmp/{caffId}")
    public ResponseEntity<CaffDTO> getTmpBmpByCaffIdWithAuth(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        return ResponseEntity.ok(caffService.getTmpBmpByCaffId(caffId));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/caff/tmp/bmp")
    public ResponseEntity<List<CaffDTO>> getTmpAllBmpWithAuth(){
        return ResponseEntity.ok(caffService.getTmpBmps());
    }
}
