package svkreml.translate.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class YaTranslateResponseDto {
    protected List<TranslationDto> translations;


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TranslationDto {
        protected String text;
        protected String detectedLanguageCode;
    }
}
