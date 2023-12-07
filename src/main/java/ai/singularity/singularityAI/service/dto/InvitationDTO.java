package ai.singularity.singularityAI.service.dto;
import lombok.Data;

@Data
public class InvitationDTO {
	private String email;
	private String position;
	private Long projectID;
	private Long inviterID;
}
