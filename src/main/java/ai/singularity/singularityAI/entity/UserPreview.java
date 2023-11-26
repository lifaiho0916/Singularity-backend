package ai.singularity.singularityAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "user_preview")
public class UserPreview implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "user_preview_id_seq")
    @SequenceGenerator(name = "user_preview_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "stopped_at")
    private Date stoppedAt;

    @Column(name = "stopped_by")
    private String stoppedBy;

    @Column(name = "build_id")
    private String buildId;

}
