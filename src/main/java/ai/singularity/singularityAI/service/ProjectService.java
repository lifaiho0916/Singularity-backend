package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.Member;
import ai.singularity.singularityAI.entity.Project;
import ai.singularity.singularityAI.entity.User;
import ai.singularity.singularityAI.entity.Template;
import ai.singularity.singularityAI.repository.MemberRepository;
import ai.singularity.singularityAI.repository.ProjectRepository;
import ai.singularity.singularityAI.repository.TemplateRepository;
import ai.singularity.singularityAI.service.dto.ProjectDTO;

import org.json.JSONObject;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    
    private final TemplateRepository templateRepository;
    
    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepository projectRepository, TemplateRepository templateRepository, MemberRepository memberRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.templateRepository = templateRepository;
        this.memberRepository = memberRepository;
        
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

    public ProjectDTO save(ProjectDTO projectDTO, Long templateId) {
        Project result = projectRepository.save(modelMapper.map(projectDTO, Project.class));
        Template template = templateRepository.getById(templateId);
        JSONObject templateJSON = new JSONObject(template.getData());
        JSONObject projectObject = templateJSON.getJSONObject("project");
        projectObject.put("id", result.getId());
        projectObject.put("name", result.getName());
        result.setData(templateJSON.toString());
        Project result1 = projectRepository.save(result);
        return modelMapper.map(result1, ProjectDTO.class);
    }

    public Optional<ProjectDTO> findById(Long projectID) {
        return projectRepository.findById(projectID).map(pro -> modelMapper.map(pro, ProjectDTO.class));
    }
    
    public void setOpenAtById(Long projectID) {
    	Optional<Project> project = projectRepository.findById(projectID);
    	if(project.get() != null) {
    		Project currentProject = project.get();
    		currentProject.setOpenedAt(new Date());
    		projectRepository.save(currentProject);
    	}
    }

    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }
    
    public List<ProjectDTO> findByCreatorId(Long creatorID) {
    	return projectRepository.findByCreatorId(creatorID).stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }
    
    public List<ProjectDTO> findByMemberUser(Long userId) {
    	return projectRepository.findByMemberUser(userId).stream().map(project -> modelMapper.map(project, ProjectDTO.class)).toList();
    }
    
    public void deleteById(Long projectID) {
    	Optional<Project> project = projectRepository.findById(projectID);
    	List<Long> idsToDelete = null;
    	if(project.get() != null) {
    		Project currentProject = project.get();
    		List<Member> members = currentProject.getMembers();
    		idsToDelete = members.stream().map(member -> member.getId()).toList();
    	}
    	projectRepository.deleteById(projectID);
    	memberRepository.deleteAllById(idsToDelete);
    }
}
