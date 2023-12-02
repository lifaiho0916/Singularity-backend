package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.audit.DateAudit;
import ai.singularity.singularityAI.entity.enums.AuthProviderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tb_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;
    
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String first_name;
    
    @Column(name = "last_name")
    private String last_name;
    
    @Column(name = "avatar")
    private String avatar;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProviderEnum provider;

    @Column(name = "provider_id")
    private String providerId;

//    @OneToMany(mappedBy = "user")
//    private List<Project> userProjects;
//
}
