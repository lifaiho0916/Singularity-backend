package ai.singularity.singularityAI.resource;

import ai.singularity.singularityAI.service.ProjectService;
import ai.singularity.singularityAI.service.dto.ProjectDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@AllArgsConstructor
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/project")
public class ProjectResource {

    @Autowired
    private ProjectService projectService;

    /**
     * GET /v1/project : Get all assets
     * Get all assets
     *
     * @return Successful operation (status code 200)
     */
    @GetMapping(
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProjectDTO>> getAllAssets(

    ) {
        List<ProjectDTO> result = projectService.findAll();
        return ResponseEntity.ok(result);
    }
    
    /**
     * GET /v1/project/created-by/{creatorId} : Get assets by creator
     * Get assets by creator
     *
     * @return Successful operation (status code 200)
     */
    @GetMapping(
    		value = "/created-by/{creatorId}",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ProjectDTO>> getAssetsbyCreatorId(
    		@PathVariable("creatorId") Long creatorId
    ) {
        List<ProjectDTO> result = projectService.findByCreatorId(creatorId);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /v1/project/{projectID} : Get asset by ID
     * Get asset by ID
     *
     * @param projectID ID of process (required)
     * @return Successful operation (status code 200)
     * or Not found (status code 404)
     */
    @GetMapping(
            value = "/{projectID}",
            produces = {"application/json"}
    )
    public ResponseEntity<ProjectDTO> getAssetByID(
            @PathVariable("projectID") Long projectID
    ) {
        var result = projectService.findById(projectID);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /v1/project : Add a new asset
     * Add a new asset
     *
     * @param projectDTO Add a new asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    @PostMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<ProjectDTO> createAsset(
            @Valid @RequestBody ProjectDTO projectDTO
    ) {
        ProjectDTO saved = projectService.save(projectDTO);
        return ResponseEntity.ok(saved);
    }

    /**
     * POST /v1/project : Update asset
     * Update asset
     *
     * @param projectDTO Update asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    @PutMapping(
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<ProjectDTO> updateAsset(
            @Valid @RequestBody ProjectDTO projectDTO
    ) {
        if (projectDTO.getId() == null)
            return ResponseEntity.badRequest().build();
        ProjectDTO saved = projectService.save(projectDTO);
        return ResponseEntity.ok(saved);
    }
}