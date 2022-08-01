package it.finanze.sanita.fse2.ms.gtw.garbage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * Maps every ms property necessary to this microservice.
 * 
 * @author Simone Lungarella
 */
@Getter
@Configuration
public class MicroservicesURLCFG {
    
    /** 
     *  Gtw-Config host.
     */
	@Value("${ms.url.gtw-config}")
	private String configHost;
}
