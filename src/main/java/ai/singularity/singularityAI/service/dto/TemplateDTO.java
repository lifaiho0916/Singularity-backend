package ai.singularity.singularityAI.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String data;
}
