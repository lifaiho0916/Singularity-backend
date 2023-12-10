package ai.singularity.singularityAI.service.dto;

import lombok.Data;

@Data
public class ProjectRequestDTO {
	private String name;
    private String description;
    private UserDTO creator;
    private Long templateId;
}
