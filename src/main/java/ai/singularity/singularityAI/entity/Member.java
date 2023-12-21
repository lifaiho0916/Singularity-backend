package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.enums.PositionEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "members")
public class Member {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private PositionEnum position;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
