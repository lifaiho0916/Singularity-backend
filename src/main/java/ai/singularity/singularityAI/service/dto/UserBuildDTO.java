package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserBuildDTO implements Serializable {

    private Long id;

    private Long projectId;

    private Double price;

    private Date createdAt;

    private String createdBy;

    private Date stoppedAt;

    private String stoppedBy;

    private String buildId;
}
