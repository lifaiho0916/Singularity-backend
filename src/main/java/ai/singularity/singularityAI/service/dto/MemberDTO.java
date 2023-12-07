package ai.singularity.singularityAI.service.dto;

import ai.singularity.singularityAI.entity.enums.PositionEnum;
import lombok.Data;

@Data
public class MemberDTO {
    private String email;
    private PositionEnum position;
}