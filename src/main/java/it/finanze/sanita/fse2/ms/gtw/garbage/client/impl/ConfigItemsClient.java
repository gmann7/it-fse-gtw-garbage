/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.finanze.sanita.fse2.ms.gtw.garbage.config.MicroservicesURLCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Client that consent to call gtw-config to retrieve configuration items.
 */
@Slf4j
@Component
public class ConfigItemsClient implements IConfigItemsClient {
    
    @Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private MicroservicesURLCFG msUrlCFG;

	@Override
	public List<ConfigItemETY> getConfigurationItems() {
		log.debug("Config Client - Retrieving configuration items");

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
				log.debug("gtw-config returned status {} and {} configuration items", response.getStatusCode(), body.getSize());
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
    public static class ConfigItemDTO {

        private List<ConfigItemETY> configurationItems;

        private Integer size;
    }
}
