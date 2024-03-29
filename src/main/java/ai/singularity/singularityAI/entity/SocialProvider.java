package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.enums.AuthProviderEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "social_providers")
public class SocialProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private AuthProviderEnum provider;
	
	@Column(name = "sub")
	private String sub;
}
