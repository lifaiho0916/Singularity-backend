package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.UserPreview;
import ai.singularity.singularityAI.repository.UserPreviewRepository;
import ai.singularity.singularityAI.service.dto.UserPreviewDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPreviewService {

    private final UserPreviewRepository userPreviewRepository;

    private final ModelMapper modelMapper;

    public UserPreviewService(UserPreviewRepository userPreviewRepository, ModelMapper modelMapper) {
        this.userPreviewRepository = userPreviewRepository;
        this.modelMapper = modelMapper;
    }

    public UserPreviewDTO save(UserPreviewDTO userPreviewDTO) {
        UserPreview result = userPreviewRepository.save(modelMapper.map(userPreviewDTO, UserPreview.class));
        return modelMapper.map(result, UserPreviewDTO.class);
    }

    public Optional<UserPreviewDTO> findById(Long projectID) {
        return userPreviewRepository.findById(projectID).map(userPreview -> modelMapper.map(userPreview, UserPreviewDTO.class));
    }

    public List<UserPreviewDTO> findAll() {
        return userPreviewRepository.findAll().stream().map(userPreview -> modelMapper.map(userPreview, UserPreviewDTO.class)).toList();
    }
}
