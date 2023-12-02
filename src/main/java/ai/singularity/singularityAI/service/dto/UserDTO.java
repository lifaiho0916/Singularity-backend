package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {

    private Long id;

    private String email;

    private String first_name;
    
    private String last_name;
    
    private String avatar;

    private Date createdAt;

    private Date updatedAt;
}
