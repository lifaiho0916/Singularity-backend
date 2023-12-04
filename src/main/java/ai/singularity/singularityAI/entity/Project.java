package ai.singularity.singularityAI.entity;

//import ai.singularity.singularityAI.entity.converter.JpaConverterJson;
import jakarta.persistence.*;
import lombok.Data;
//import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tb_projects")
public class Project {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
    
    @Column(name = "opened_at")
    private Date openedAt;
    
    @ManyToMany
    private List<Member> members;
    
//    @Convert(converter = JpaConverterJson.class)
//    @Column(name = "data", nullable = false, columnDefinition = "jsonb")
//    @ColumnTransformer(write = "?::jsonb")
//    private String data = "{}";
}
