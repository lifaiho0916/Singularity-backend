package ai.singularity.singularityAI.repository;

import ai.singularity.singularityAI.entity.Media;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
	List<Media> findByProjectId(Long projectId);
}