package it.finanze.sanita.fse2.ms.gtw.garbage.config.mongo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Component
@EqualsAndHashCode(callSuper = false)
public class MongoPropertiesFseCFG implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5927138450543939809L;

	@Value("${valdoc.mongodb.uri}")
	private String uri;

}
