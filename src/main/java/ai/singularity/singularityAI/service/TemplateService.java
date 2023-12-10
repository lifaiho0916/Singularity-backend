package ai.singularity.singularityAI.service;

import ai.singularity.singularityAI.entity.Template;
import ai.singularity.singularityAI.repository.TemplateRepository;
import ai.singularity.singularityAI.service.dto.TemplateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    private final ModelMapper modelMapper;

    public TemplateService(TemplateRepository templateRepository, ModelMapper modelMapper) {
        this.templateRepository = templateRepository;
        this.modelMapper = modelMapper;
    }

    public TemplateDTO save(TemplateDTO templateDTO) {
        Template result = templateRepository.save(modelMapper.map(templateDTO, Template.class));
        return modelMapper.map(result, TemplateDTO.class);
    }

    public Optional<TemplateDTO> findById(Long templateID) {
        return templateRepository.findById(templateID).map(template -> modelMapper.map(template, TemplateDTO.class));
    }

    public List<TemplateDTO> findAll() {
        return templateRepository.findAll().stream().map(template -> modelMapper.map(template, TemplateDTO.class)).toList();
    }
}
