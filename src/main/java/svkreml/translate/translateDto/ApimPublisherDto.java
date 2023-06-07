package svkreml.translate.translateDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;

@Setter
@Getter
@ToString
public class ApimPublisherDto extends LinkedHashMap<String, List<ApimPublisherDto.Translate>> {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Translate {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<Translate> children;
        Integer type;
        String value;
    }
}
