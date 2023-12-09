package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.converter.JpaConverterJson;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_templates")
public class Template implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Convert(converter = JpaConverterJson.class)
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String data;
}
