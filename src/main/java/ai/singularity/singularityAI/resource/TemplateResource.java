package ai.singularity.singularityAI.resource;

import ai.singularity.singularityAI.service.TemplateService;
import ai.singularity.singularityAI.service.dto.TemplateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Validated
@AllArgsConstructor
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/template")
public class TemplateResource {

    @Autowired
    private TemplateService templateService;

    /**
     * GET /v1/project : Get all assets
     * Get all assets
     *
     * @return Successful operation (status code 200)
     */
    @GetMapping(
            produces = {"application/json"}
    )
    public ResponseEntity<List<TemplateDTO>> getAllTemplates(

    ) {
        List<TemplateDTO> result = templateService.findAll();
        return ResponseEntity.ok(result);
    }

    /**
     * GET /v1/project/{templateID} : Get asset by ID
     * Get asset by ID
     *
     * @param templateID ID of process (required)
     * @return Successful operation (status code 200)
     * or Not found (status code 404)
     */
    @GetMapping(
            value = "/{templateID}",
            produces = {"application/json"}
    )
    public ResponseEntity<TemplateDTO> getTemplateByID(
            @PathVariable("templateID") Long templateID
    ) {
        var result = templateService.findById(templateID);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /v1/project : Add a new asset
     * Add a new asset
     *
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<TemplateDTO> createTemplate(
            @Valid @RequestBody TemplateDTO templateDTO
    ) {
        if (templateDTO.getId() != null)
            return ResponseEntity.badRequest().build();
        TemplateDTO saved = templateService.save(templateDTO);
        return ResponseEntity.ok(saved);
    }

    /**
     * POST /v1/project : Update asset
     * Update asset
     *
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    @PutMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<TemplateDTO> updateTemplate(
            @Valid @RequestBody TemplateDTO templateDTO
    ) {
        if (templateDTO.getId() == null)
            return ResponseEntity.badRequest().build();
        TemplateDTO saved = templateService.save(templateDTO);
        return ResponseEntity.ok(saved);
    }
}
