package ai.singularity.singularityAI.resource;

import ai.singularity.singularityAI.service.UserService;
import ai.singularity.singularityAI.service.dto.UserDTO;
import ai.singularity.singularityAI.security.UserPrincipal;
import ai.singularity.singularityAI.security.CurrentUser;
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
@RequestMapping("v1/user")
public class UserResource {

    @Autowired
    private UserService userService;
    
    @GetMapping(
    		value = "/me",
    		produces = {"application/json"}
    )
    public ResponseEntity<UserDTO> getCurrentUser(
    		@CurrentUser UserPrincipal userPrincipal
    ) {
    	 UserDTO currentUser = userService.getCurrentUser(userPrincipal.getId());
    	 return ResponseEntity.ok(currentUser);
    }

    /**
     * GET /v1/project : Get all assets
     * Get all assets
     *
     * @return Successful operation (status code 200)
     */
    @GetMapping(
            value = "/list",
            produces = {"application/json"}
    )
    public ResponseEntity<List<UserDTO>> getAllAssets(

    ) {
        List<UserDTO> saved = userService.findAll();
        return ResponseEntity.ok(saved);
    }

    /**
     * GET /v1/project/{userID} : Get asset by ID
     * Get asset by ID
     *
     * @param userID ID of process (required)
     * @return Successful operation (status code 200)
     * or Not found (status code 404)
     */
    @GetMapping(
            value = "/v1/user/{userID}",
            produces = {"application/json"}
    )
    public ResponseEntity<UserDTO> getAssetByID(
            @PathVariable("userID") Long userID
    ) {
        var result = userService.findById(userID);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * POST /v1/project : Add a new asset
     * Add a new asset
     *
     * @param userDTO Add a new asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    @PostMapping(
            value = "/v1/user",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDTO> createAsset(
            @Valid @RequestBody UserDTO userDTO
    ) {
        if (userDTO.getId() != null)
            return ResponseEntity.badRequest().build();
        UserDTO saved = userService.save(userDTO);
        return ResponseEntity.ok(saved);
    }
}
