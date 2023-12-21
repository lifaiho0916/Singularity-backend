package ai.singularity.singularityAI.entity;

import ai.singularity.singularityAI.entity.enums.AuthProviderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

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
    
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
