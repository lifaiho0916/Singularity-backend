package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.Project;
import ai.singularity.singularityAI.entity.User;
import ai.singularity.singularityAI.repository.ProjectRepository;
import ai.singularity.singularityAI.service.dto.ProjectDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        Converter<ProjectDTO, Project> projectConverter = new Converter<ProjectDTO, Project>() {
            @Override
            public Project convert(MappingContext<ProjectDTO, Project> mappingContext) {
                ProjectDTO source = mappingContext.getSource();

                User user = new User();
                user.setId(source.getCreator().getId());

                Project project = new Project();
                project.setName(source.getName());
                project.setDescription(source.getDescription());
                project.setCreator(user);
                return project;
            }
        };

        this.modelMapper.addConverter(projectConverter);
    }

    public ProjectDTO save(ProjectDTO projectDTO) {
        Project result = projectRepository.save(modelMapper.map(projectDTO, Project.class));
        return modelMapper.map(result, ProjectDTO.class);
    }

    public Optional<ProjectDTO> findById(Long projectID) {
        return projectRepository.findById(projectID).map(project -> modelMapper.map(project, ProjectDTO.class));
    }

    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }
    
    public List<ProjectDTO> findByCreatorId(Long creatorId) {
    	return projectRepository.findByCreatorId(creatorId).stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }
}
