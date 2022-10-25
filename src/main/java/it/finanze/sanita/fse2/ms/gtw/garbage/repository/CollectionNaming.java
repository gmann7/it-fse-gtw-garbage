/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.ProfileUtility;

@Configuration
public class CollectionNaming {

    @Autowired
    private ProfileUtility profileUtility;

    @Bean("transactionEventsBean")
    public String getTransactionDataCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.Collections.TRANSACTION_DATA;
        }
        return Constants.Collections.TRANSACTION_DATA;
    }
    
    @Bean("iniEdsInvocationBean")
    public String getIniEdsCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.Collections.INI_EDS_INVOCATION;
        }
        return Constants.Collections.INI_EDS_INVOCATION;
    }
}
