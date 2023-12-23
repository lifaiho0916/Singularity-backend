package ai.singularity.singularityAI.repository;

import ai.singularity.singularityAI.entity.Media;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	List<Media> findByProjectId(Long projectId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM medias m WHERE m.project_id = :projectId", nativeQuery = true)
	void deleteByProjectId(@Param("projectId") Long projectId);
}