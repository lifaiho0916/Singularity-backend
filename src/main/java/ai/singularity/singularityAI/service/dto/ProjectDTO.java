package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.util.Date;

import ai.singularity.singularityAI.entity.User;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private UserDTO creator;
    private Date createdAt;
    private Date updatedAt;
    private Date openedAt;
}
