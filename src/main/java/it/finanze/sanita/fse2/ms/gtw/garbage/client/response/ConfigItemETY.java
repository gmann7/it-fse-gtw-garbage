/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.client.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration item entity received from gtw-config.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigItemETY {
    
    private String key;

    private Map<String, String> items;
}
