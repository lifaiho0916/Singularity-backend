package ai.singularity.singularityAI.repository;

import ai.singularity.singularityAI.entity.UserBuild;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface UserBuildRepository extends JpaRepository<UserBuild, Long> {

}