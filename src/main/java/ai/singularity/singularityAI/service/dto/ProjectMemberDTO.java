package ai.singularity.singularityAI.service.dto;

import ai.singularity.singularityAI.entity.enums.PositionEnum;
import lombok.Data;

@Data
public class ProjectMemberDTO {
    private UserDTO user;
    private PositionEnum position;
}