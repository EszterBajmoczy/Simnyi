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

    @PostMapping(path = "caff", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> upload(@Validated @NonNull @RequestParam(name = "name") String name,
                                       @Validated @NonNull @RequestBody MultipartFile file) {
        if(file == null) {
            return ResponseEntity.badRequest().build();
        }

        caffService.create(name, file);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/caff/modify")
    public ResponseEntity<Void> modify(@Validated @RequestBody CaffUpdateDTO dto) {
        caffService.modify(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/caff/delete/{caffId}")
    public ResponseEntity<Void> delete(@Validated @NotBlank @PathVariable(name = "caffId") String caffId) {
        caffService.delete(caffId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/caff/{caffId}")
    public ResponseEntity<String> downloadCaffById(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        var result = caffService.findCaffById(caffId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/caff/bmp/{caffId}")
    public ResponseEntity<CaffDTO> getBmpByCaffId(@Validated @NotBlank @PathVariable(name = "caffId") String caffId){
        return ResponseEntity.ok(caffService.getBmpByCaffId(caffId));
    }

    @GetMapping(path = "/caff/bmp/search/{searchInName}")
    public ResponseEntity<List<CaffDTO>> searchAllBmp(@PathVariable(name = "searchInName") String searchInName){
        return ResponseEntity.ok(caffService.getBmps(searchInName));
    }

    @GetMapping(path = "/caff/bmp")
    public ResponseEntity<List<CaffDTO>> getAllBmp(){
        return ResponseEntity.ok(caffService.getAllBmps());
    }
}
