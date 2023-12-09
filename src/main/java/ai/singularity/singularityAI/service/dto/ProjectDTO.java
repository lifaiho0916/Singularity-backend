package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import ai.singularity.singularityAI.entity.User;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private UserDTO creator;
    private List<ProjectMemberDTO> members;
    private String data;
    private Date createdAt;
    private Date updatedAt;
    private Date openedAt;
}
