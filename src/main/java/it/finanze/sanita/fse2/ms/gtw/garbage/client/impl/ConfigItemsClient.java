package it.finanze.sanita.fse2.ms.gtw.garbage.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.client.response.ConfigItemETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.MicroservicesURLCFG;
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
@Profile("!" + Constants.Profile.DEV)
public class ConfigItemsClient implements IConfigItemsClient {
    
    @Autowired
    private transient RestTemplate restTemplate;
	
	@Autowired
	private MicroservicesURLCFG msUrlCFG;

	@Override
	public List<ConfigItemETY> getConfigurationItems() {
		log.info("Calling gtw-config");

        List<ConfigItemETY> configurationItems = new ArrayList<>();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<?> entity = new HttpEntity<>(headers);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(msUrlCFG.getConfigHost() + "/v1/config-items")
            .queryParam("type", "GARBAGE");

		ResponseEntity<ConfigItemDTO> response = null;
		try {
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, ConfigItemDTO.class);
			final ConfigItemDTO body = response.getBody();
			if (body != null) {
				configurationItems = body.getConfigurationItems();
				log.info("gtw-config returned status {} and {} configuration items", response.getStatusCode(), body.getSize());
			} else {
				log.warn("gtw-config returned status {} and null body", response.getStatusCode());
			}
		} catch(HttpClientErrorException cex) {
			log.error(String.format("Error encountered on gtw-config, received %s status code", cex.getStatusCode()), cex);
			throw cex;
		} catch(Exception ex) {
			log.error("Generic error while calling gtw-config", ex);
			throw new BusinessException("Generic error while calling gtw-config", ex);
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
