package ai.singularity.singularityAI;

import ai.singularity.singularityAI.entity.Project;
import ai.singularity.singularityAI.entity.User;
import ai.singularity.singularityAI.entity.enums.CompanyTypeEnum;
import ai.singularity.singularityAI.entity.enums.UserRoleEnum;
import ai.singularity.singularityAI.repository.ProjectRepository;
import ai.singularity.singularityAI.repository.UserRepository;
import ai.singularity.singularityAI.service.dto.ProjectDTO;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectResourceTest {

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private User user = null;

    @BeforeEach
    void beforeTest() {
        User user = new User();
//        user.setCompanyName("test");
        user.setEmail("daco@daco.sk");
//        user.setRole(UserRoleEnum.SOFTWARE_DEVELOPER);
//        user.setCompanyType(CompanyTypeEnum.SOFTWARE_DEVELOPMENT);
//        user.setFullName("daco daco");
        Optional<User> existedUser = userRepository.findByEmail("daco@daco.sk");
        this.user = existedUser.orElseGet(() -> userRepository.save(user));
    }

    @Test
    void testAddProject() throws Exception {

        final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("name");

        // Run the test
        mockMvc.perform(post("/v1/project")
                        .content(ow.writeValueAsString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(projectDTO.getName()));
    }

    @Test
    void testUpdateProject() throws Exception {

        final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("name");

        Project saved = projectRepository.save(modelMapper.map(projectDTO, Project.class));

        projectDTO.setId(saved.getId());
        projectDTO.setName("Updated name");

        // Run the test
        mockMvc.perform(put("/v1/project")
                        .content(ow.writeValueAsString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value(projectDTO.getName()));
    }

    @Test
    void testGetProjectByID() throws Exception {

        final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("name");

        Project saved = projectRepository.save(modelMapper.map(projectDTO, Project.class));


        // Run the test
        mockMvc.perform(get("/v1/project/{projectID}", saved.getId())
                        .content(ow.writeValueAsString(projectDTO)
                        ).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value(saved.getName()));
    }

    @Test
    void testGetAllProjects() throws Exception {

        final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setName("name");

        Project saved = projectRepository.save(modelMapper.map(projectDTO, Project.class));

        // Run the test
        mockMvc.perform(get("/v1/project")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(saved.getName())));
    }
}
