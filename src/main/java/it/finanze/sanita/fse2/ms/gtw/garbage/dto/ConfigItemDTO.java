package it.finanze.sanita.fse2.ms.gtw.garbage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ConfigItemDTO {

    private String traceId;
    private String spanId;

    private List<ConfigDataItemDTO> configurationItems;

    private Integer size;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConfigDataItemDTO {
        private String key;
        private Map<String, String> items;
    }

}
