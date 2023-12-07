package ai.singularity.singularityAI.service.dto;

import java.util.List;
import lombok.Data;

@Data
public class InviteMemberDTO {
    private List<MemberDTO> members;
}