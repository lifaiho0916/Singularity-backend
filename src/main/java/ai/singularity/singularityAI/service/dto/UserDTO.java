package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {

    private Long id;

    private String email;

    private String fullName;

    private String companyName;

    private String companyType;

    private String role;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;
}
