package it.finanze.sanita.fse2.ms.gtw.garbage.client;

import java.util.List;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.response.ConfigItemETY;

public interface IConfigItemsClient {

    List<ConfigItemETY> getConfigurationItems();
}
