package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.Date;

public interface ICfgItemsRetentionRepo extends Serializable {

	Integer deleteCfgItems(Date dateToRemove, Class<?> clazz);
}
