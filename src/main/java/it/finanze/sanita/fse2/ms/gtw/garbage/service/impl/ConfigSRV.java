package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.dto.ConfigItemDTO;
import it.finanze.sanita.fse2.ms.gtw.garbage.enums.ConfigItemTypeEnum;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IConfigSRV;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.finanze.sanita.fse2.ms.gtw.garbage.client.routes.base.ClientRoutes.Config.PROPS_NAME_ITEMS_RETENTION_DAY;
import static it.finanze.sanita.fse2.ms.gtw.garbage.client.routes.base.ClientRoutes.Config.PROPS_NAME_VALD_DOCS_RETENTION_DAY;
import static it.finanze.sanita.fse2.ms.gtw.garbage.dto.ConfigItemDTO.ConfigDataItemDTO;
import static it.finanze.sanita.fse2.ms.gtw.garbage.enums.ConfigItemTypeEnum.*;

@Slf4j
@Service
public class ConfigSRV implements IConfigSRV {

    private static final long DELTA_MS = 300_000L;

    @Autowired
    private IConfigItemsClient client;

    private final Map<String, Pair<Long, String>> props;

    public ConfigSRV() {
        this.props = new HashMap<>();
    }

    @PostConstruct
    public void postConstruct() {
        for(ConfigItemTypeEnum en : priority()) {
            log.info("[GTW-CFG] Retrieving {} properties ...", en.name());
            ConfigItemDTO items = client.getConfigurationItems(en);
            List<ConfigDataItemDTO> opts = items.getConfigurationItems();
            for(ConfigDataItemDTO opt : opts) {
                opt.getItems().forEach((key, value) -> {
                    log.info("[GTW-CFG] Property {} is set as {}", key, value);
                    props.put(key, Pair.of(new Date().getTime(), value));
                });
                if(opt.getItems().isEmpty()) log.info("[GTW-CFG] No props were found");
            }
        }
        integrity();
    }


    @Override
    public Integer getValidatedDocRetentionDay() {
        long lastUpdate = props.get(PROPS_NAME_VALD_DOCS_RETENTION_DAY).getKey();
        if (new Date().getTime() - lastUpdate >= DELTA_MS) {
            synchronized(PROPS_NAME_VALD_DOCS_RETENTION_DAY) {
                if (new Date().getTime() - lastUpdate >= DELTA_MS) {
                    refresh(PROPS_NAME_VALD_DOCS_RETENTION_DAY);
                }
            }
        }
        return Integer.parseInt(
            props.get(PROPS_NAME_VALD_DOCS_RETENTION_DAY).getValue()
        );
    }

    @Override
    public Integer getConfigItemsRetentionDay() {
        long lastUpdate = props.get(PROPS_NAME_ITEMS_RETENTION_DAY).getKey();
        if (new Date().getTime() - lastUpdate >= DELTA_MS) {
            synchronized(PROPS_NAME_ITEMS_RETENTION_DAY) {
                if (new Date().getTime() - lastUpdate >= DELTA_MS) {
                    refresh(PROPS_NAME_ITEMS_RETENTION_DAY);
                }
            }
        }
        return Integer.parseInt(
            props.get(PROPS_NAME_ITEMS_RETENTION_DAY).getValue()
        );
    }

    private void refresh(String name) {
        String previous = props.getOrDefault(name, Pair.of(0L, null)).getValue();
        String prop = client.getProps(name, previous, GARBAGE);
        props.put(name, Pair.of(new Date().getTime(), prop));
    }

    private void integrity() {
        String err = "Missing props {} from garbage";
        String[] out = new String[]{
            PROPS_NAME_VALD_DOCS_RETENTION_DAY,
            PROPS_NAME_ITEMS_RETENTION_DAY
        };
        for (String prop : out) {
            if(!props.containsKey(prop)) throw new IllegalStateException(err.replace("{}", prop));
        }
    }

}
