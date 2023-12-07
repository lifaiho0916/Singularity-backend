package ai.singularity.singularityAI.repository;

import ai.singularity.singularityAI.entity.Project;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	List<Project> findByCreatorId(Long creatorId);
	
	@Query(value = "SELECT p.* FROM tb_projects p"
			+ " JOIN tb_projects_members pm ON p.id = pm.project_id"
			+ " JOIN tb_members m ON pm.members_id = m.id"
			+ " JOIN tb_users u ON m.user_id = u.id WHERE u.id = :userId", nativeQuery = true)
	List<Project> findByMemberUser(@Param("userId") Long userId);
}