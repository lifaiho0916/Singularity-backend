package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.converter.JpaConverterJson;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(generator = "project_id_seq")
    @SequenceGenerator(name = "project_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "creator_id")
    @ManyToOne
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Convert(converter = JpaConverterJson.class)
    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private String data = "{}";

//    @ManyToMany
//    private List<User> users;
}
