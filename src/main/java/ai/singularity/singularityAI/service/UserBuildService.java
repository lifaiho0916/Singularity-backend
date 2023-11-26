package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.UserBuild;
import ai.singularity.singularityAI.repository.UserBuildRepository;
import ai.singularity.singularityAI.service.dto.UserBuildDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserBuildService {

    private final UserBuildRepository userBuildRepository;

    private final ModelMapper modelMapper;

    public UserBuildService(UserBuildRepository userBuildRepository, ModelMapper modelMapper) {
        this.userBuildRepository = userBuildRepository;
        this.modelMapper = modelMapper;
    }

    public UserBuildDTO save(UserBuildDTO userBuildDTO) {
        UserBuild result = userBuildRepository.save(modelMapper.map(userBuildDTO, UserBuild.class));
        return modelMapper.map(result, UserBuildDTO.class);
    }

    public Optional<UserBuildDTO> findById(Long projectID) {
        return userBuildRepository.findById(projectID).map(userBuild -> modelMapper.map(userBuild, UserBuildDTO.class));
    }

    public List<UserBuildDTO> findAll() {
        return userBuildRepository.findAll().stream().map(userBuild -> modelMapper.map(userBuild, UserBuildDTO.class)).toList();
    }
}
