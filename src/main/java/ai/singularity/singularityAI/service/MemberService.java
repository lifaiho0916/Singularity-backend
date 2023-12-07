package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.User;
import ai.singularity.singularityAI.exception.ResourceNotFoundException;
import ai.singularity.singularityAI.repository.UserRepository;
import ai.singularity.singularityAI.service.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public MemberService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    
    public UserDTO getCurrentUser(Long userId) {
    	return userRepository.findById(userId).map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    public UserDTO save(UserDTO userDTO) {
        User result = userRepository.save(modelMapper.map(userDTO, User.class));
        return modelMapper.map(result, UserDTO.class);
    }

    public Optional<UserDTO> findById(Long projectID) {
        return userRepository.findById(projectID).map(user -> modelMapper.map(user, UserDTO.class));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }
}
