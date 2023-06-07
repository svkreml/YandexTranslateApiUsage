package svkreml.translate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import svkreml.translate.translateDto.ApimPublisherDto;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class WriteTranslatedMain {

    public static void main(String[] args) throws Exception {


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        final ApimPublisherDto apimPublisherDto = mapper.readValue(new FileInputStream("tests/en.json"), ApimPublisherDto.class);
        final Map<String, String> translateMap = mapper.readValue(new FileInputStream("output-1.json"), new TypeReference<Map<String, String>>() {
        });


        for (Map.Entry<String, List<ApimPublisherDto.Translate>> stringTranslateEntry : apimPublisherDto.entrySet()) {
            final List<ApimPublisherDto.Translate> translates = stringTranslateEntry.getValue();
            for (ApimPublisherDto.Translate translate : translates) {
                translate.setValue(translateMap.get(translate.getValue()));
                if (translate.getChildren() != null) {
                    for (ApimPublisherDto.Translate child : translate.getChildren()) {
                        child.setValue(translateMap.get(child.getValue()));
                    }
                }
            }
        }


        mapper.writeValue(new File("tests/ru.json"), apimPublisherDto);
    }


}
