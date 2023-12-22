package ai.singularity.singularityAI.resource;

import ai.singularity.singularityAI.elasticEmail.ElasticEmailClient;
import ai.singularity.singularityAI.entity.Media;
import ai.singularity.singularityAI.entity.Member;
import ai.singularity.singularityAI.entity.Project;
import ai.singularity.singularityAI.entity.User;
import ai.singularity.singularityAI.entity.enums.PositionEnum;
import ai.singularity.singularityAI.repository.MediaRepository;
import ai.singularity.singularityAI.repository.MemberRepository;
import ai.singularity.singularityAI.repository.ProjectRepository;
import ai.singularity.singularityAI.repository.UserRepository;
import ai.singularity.singularityAI.security.CurrentUser;
import ai.singularity.singularityAI.security.TokenProvider;
import ai.singularity.singularityAI.security.UserPrincipal;
import ai.singularity.singularityAI.service.ProjectService;
import ai.singularity.singularityAI.service.UserService;
import ai.singularity.singularityAI.service.dto.AcceptInvitationDTO;
import ai.singularity.singularityAI.service.dto.InvitationDTO;
import ai.singularity.singularityAI.service.dto.InviteMemberDTO;
import ai.singularity.singularityAI.service.dto.MediaDTO;
import ai.singularity.singularityAI.service.dto.ProjectDTO;
import ai.singularity.singularityAI.service.dto.ProjectMemberDTO;
import ai.singularity.singularityAI.service.dto.ProjectRequestDTO;
import ai.singularity.singularityAI.service.dto.ProjectDataDTO;
import ai.singularity.singularityAI.service.dto.UploadProjectImageRequestDTO;
import ai.singularity.singularityAI.service.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.json.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Validated
@AllArgsConstructor
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/project")
public class ProjectResource {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private MediaRepository mediaRepository;

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
    	List<ProjectDTO> combinedList = new ArrayList<>();
        List<ProjectDTO> result = projectService.findByCreatorId(creatorId);
        List<ProjectDTO> result1 = projectService.findByMemberUser(creatorId);
        
        combinedList.addAll(result);
        combinedList.addAll(result1);
        return ResponseEntity.ok(combinedList);
    }
    
    /**
     * POST /v1/project/{projectID}/invite : Send Invitation mails
     * Send Invitation mails
     *
     * @return Successful operation (status code 200)
     */
    @PostMapping(
    		value = "/{projectID}/invite"
    )
    public ResponseEntity<String> sendInvitationEmail(
    		@PathVariable("projectID") Long projectID,
    		@Valid @RequestBody InviteMemberDTO inviteMemberDTO,
    		@CurrentUser UserPrincipal userPrincipal
    ) {
    	UserDTO currentUser = userService.getCurrentUser(userPrincipal.getId());
    	ProjectDTO projectDTO = projectService.findById(projectID).get();
    	inviteMemberDTO.getMembers().forEach(member -> {
    		String token = tokenProvider.createInvitationToken(member, projectDTO.getId(), currentUser.getId());
    		String clientUrl = environment.getProperty("app.clientUrl");
    		String result = ElasticEmailClient.Send(
    			currentUser.getEmail(),
    			currentUser.getFirst_name() + " " + currentUser.getLast_name(),
    			 "You have been invited as " + member.getPosition().toString() + " on Singularity Project: " + projectDTO.getName(), 
    			 String.format("<a href=\"%s\">Accept Invitation</a>", clientUrl + "/invite?token=" + token), 
    			 member.getEmail(),
    			 "true"
    		);
    		System.out.println(result);
    	});
        return ResponseEntity.ok("The invitation email has been successfully delivered");
    }
    
    @PostMapping(
    		value = "/accept-invitation"
    )
    public ResponseEntity<String> AcceptInvitation(
    		@Valid @RequestBody AcceptInvitationDTO acceptInvitationDTO,
    		@CurrentUser UserPrincipal userPrincipal
    ) {
    	String token = acceptInvitationDTO.getToken();
    	if(tokenProvider.validateToken(token)) {
    		InvitationDTO invitationDTO = tokenProvider.getInvitationInfoFromToken(token);
    		User user = userRepository.findById(userPrincipal.getId())
					.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + invitationDTO.getEmail()));
    		if(user.getEmail().equals(invitationDTO.getEmail())) {
    			Project project = projectRepository.findById(invitationDTO.getProjectID()).orElseThrow();
        		List<Member> projectMembers = project.getMembers();
        		AtomicBoolean repeated = new AtomicBoolean(false);
        		projectMembers.forEach(mem -> {
        			if(mem.getUser().getId() == user.getId()) {
        				repeated.set(true);
        			}
        		});
        		if(!repeated.get()) {
        			Member member = new Member();
            		member.setPosition(PositionEnum.valueOf(invitationDTO.getPosition()));
            		member.setUser(user);
            		Member savedMember = memberRepository.save(member);
            		
            		projectMembers.add(savedMember);
            		projectRepository.save(project);
            		return ResponseEntity.ok("Accepted invitation successfully");
        		} 
    		} 
    	}
    	return ResponseEntity.ok("Failed to Accept invitation successfully");
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
            value = "/{projectID}/open",
            produces = {"application/json"}
    )
    public void setOepnAtByID(
            @PathVariable("projectID") Long projectID
    ) {
        projectService.setOpenAtById(projectID);
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
     * Delete /v1/project/{projectID} : Delete asset by ID
     * Delete asset by ID
     *
     * @param projectID ID of process (required)
     * @return Successful operation (status code 200)
     * or Not found (status code 404)
     */
    @DeleteMapping(
            value = "/{projectID}",
            produces = {"application/json"}
    )
    public ResponseEntity<Boolean> DeleteAssetByID(
            @PathVariable("projectID") Long projectID
    ) {
    	try {
    		projectService.deleteById(projectID);
    		return ResponseEntity.ok(true);
    	} catch (Exception e) {
    		return ResponseEntity.notFound().build();
    	}
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
            @Valid @RequestBody ProjectRequestDTO projectRequestDTO
    ) {
    	ProjectDTO projectDTO = new ProjectDTO();
    	projectDTO.setName(projectRequestDTO.getName());
    	projectDTO.setDescription(projectRequestDTO.getDescription());
    	projectDTO.setCreator(projectRequestDTO.getCreator());
    	Long templateId = projectRequestDTO.getTemplateId();
        ProjectDTO saved = projectService.save(projectDTO, templateId);
    	return ResponseEntity.ok(saved);
    }
   
    /**
     * GET /v1/project/{projectID}/images : Get images by id
     * Add a new asset
     *
     * @param projectDTO Add a new asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
    */
   
    @GetMapping(
    		value = "/{projectID}/images",
            produces = {"application/json"}
    )
    public ResponseEntity<List<MediaDTO>> getProjectImage(
    		@PathVariable("projectID") Long projectID
    ) {
    	List<Media> medias = mediaRepository.findByProjectId(projectID);
    	List<MediaDTO> images = medias.stream().map(media -> {
    		MediaDTO newMediaDTO = new MediaDTO();
    		newMediaDTO.setId(media.getId());
    		newMediaDTO.setImageData(media.getImageData());
    		return newMediaDTO;
    	}).toList();
    	return ResponseEntity.ok(images);
    }
    
    /**
     * PUT /v1/project/{projectID} : Upload image by id
     * Upload 
     *
     * @param projectDataDTO update project (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */

    @PutMapping(
    		value = "/{projectID}",
    		produces = {"application/json"}
    )
    public ResponseEntity<String> updateProject(
    		@PathVariable("projectID") Long projectID,
    		@RequestBody ProjectDataDTO projectDataDTO
    ) {
    	Optional<Project> optionalProject = projectRepository.findById(projectID);
    	if (optionalProject.isPresent()) {
	        Project project = optionalProject.get();
	        project.setData(projectDataDTO.getData());
	        projectRepository.save(project);
	        return ResponseEntity.ok("ViewTree Saved successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no such a project");
	    }
    }
    
   
    
    /**
     * POST /v1/project/{projectID}/upload : Upload image by id
     * Upload 
     *
     * @param projectDTO Add a new asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
    
    @PostMapping(
    		value = "/{projectID}/upload",
            consumes = {"application/json"}
    )
    public ResponseEntity<String> uploadProjectImage(
    		@PathVariable("projectID") Long projectID,
            @Valid @RequestBody UploadProjectImageRequestDTO uploadProjectImageRequestDTO
    ) {
    	Project project = projectRepository.getById(projectID);
    	Media media = new Media();
    	media.setProject(project);
    	media.setImageData(uploadProjectImageRequestDTO.getImageData());
    	mediaRepository.save(media);
    	return ResponseEntity.ok("Success");
    }

    /**
     * POST /v1/project : Update asset
     * Update asset
     *
     * @param projectDTO Update asset (required)
     * @return Successful operation (status code 200)
     * or Validation error (status code 400)
     */
//    @PutMapping(
//            produces = {"application/json"},
//            consumes = {"application/json"}
//    )
//    public ResponseEntity<ProjectDTO> updateAsset(
//            @Valid @RequestBody ProjectDTO projectDTO
//    ) {
//        if (projectDTO.getId() == null)
//            return ResponseEntity.badRequest().build();
//        ProjectDTO saved = projectService.save(projectDTO);
//        return ResponseEntity.ok(saved);
//    }
}