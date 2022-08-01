package it.finanze.sanita.fse2.ms.gtw.garbage.client.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.client.response.ConfigItemETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Client that consent to call gtw-config to retrieve configuration items.
 * 
 * @author Simone Lungarella
 */
@Slf4j
@Component
@Profile(Constants.Profile.DEV)
public class ConfigItemsMockClient implements IConfigItemsClient {

	@Override
	public List<ConfigItemETY> getConfigurationItems() {
		log.info("Mocking call to gtw-config");

        List<ConfigItemETY> configurationItems = new ArrayList<>();
		
		try {
			Map<String, String> items = new HashMap<>();
			items.put(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS, "2");
			items.put(Constants.ConfigItems.ERROR_TRANSACTION_RETENTION_HOURS, "5");

			configurationItems.add(new ConfigItemETY("GARBAGE", items));
		} catch(Exception ex) {
			log.error("Generic error while calling mocked gtw-config", ex);
			throw new BusinessException("Generic error while calling mocked gtw-config", ex);
		}
		return configurationItems;
	}

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ConfigItemDTO {

        private List<ConfigItemETY> configurationItems;

        private Integer size;
    }
}
