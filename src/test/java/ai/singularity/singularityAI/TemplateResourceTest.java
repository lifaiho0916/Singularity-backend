package ai.singularity.singularityAI;

import ai.singularity.singularityAI.entity.Template;
import ai.singularity.singularityAI.repository.TemplateRepository;
import ai.singularity.singularityAI.service.dto.TemplateDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TemplateResourceTest {

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    void testAddTemplate() throws Exception {

        final TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setName("name");
        templateDTO.setDescription("test desc");

        // Run the test
        mockMvc.perform(post("/v1/template")
                        .content(ow.writeValueAsString(templateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(templateDTO.getName()))
                .andExpect(jsonPath("$.description").value(templateDTO.getDescription()));
    }
    
    @Test
    void testUpdateTemplate() throws Exception {

        final TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setName("name");
        templateDTO.setDescription("test desc");

        Template saved = templateRepository.save(modelMapper.map(templateDTO, Template.class));

        templateDTO.setId(saved.getId());
        templateDTO.setName("Updated name");

        // Run the test
        mockMvc.perform(put("/v1/template")
                        .content(ow.writeValueAsString(templateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(templateDTO.getName()));
    }

    @Test
    void testGetAllProcesses() throws Exception {

        final TemplateDTO templateDTO = new TemplateDTO();
        templateDTO.setName("name");
        templateDTO.setDescription("test desc");

        Template saved = templateRepository.save(modelMapper.map(templateDTO, Template.class));

        // Run the test
        mockMvc.perform(get("/v1/template")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(saved.getName())));
    }
}
