package ai.singularity.singularityAI.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter()
public class JpaConverterJson implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String meta) {
        return meta;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }

}
