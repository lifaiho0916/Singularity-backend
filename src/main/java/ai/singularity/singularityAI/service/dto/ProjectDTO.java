package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private String data;

}
