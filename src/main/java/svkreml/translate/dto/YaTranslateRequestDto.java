package svkreml.translate.dto;

import lombok.*;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class YaTranslateRequestDto {

    protected String folderId;
    protected List<String> texts;
    protected String sourceLanguageCode;
    protected String targetLanguageCode;

    // getters and setters
}
