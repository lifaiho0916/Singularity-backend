package ai.singularity.singularityAI.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter()
public class JpaConverterJson implements AttributeConverter<String, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(String meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, String.class);
        } catch (IOException ex) {
            return null;
        }
    }

}
