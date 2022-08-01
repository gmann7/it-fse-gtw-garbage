/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.config.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;

/**
 * @author AndreaPerquoti
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = Constants.ComponentScan.CONFIG_MONGO)
public class MongoDatabaseCFG {

	@Autowired
	private MongoPropertiesDataCFG mongoData;

	@Autowired
	private MongoPropertiesTransactionsCFG mongoTransactions;

    @Autowired
    private ApplicationContext appContext;
 
    final List<Converter<?, ?>> conversions = new ArrayList<>();

    @Bean
    @Primary
    @Qualifier("mongo-factory-data")
    public MongoDatabaseFactory mongoDatabaseFactoryData(){
        return new SimpleMongoClientDatabaseFactory(mongoData.getUri());
    }
    
    @Bean
    @Qualifier("mongo-factory-data")
    public MongoDatabaseFactory mongoDatabaseFactoryTransactions(){
    	return new SimpleMongoClientDatabaseFactory(mongoTransactions.getUri());
    }

    @Bean
    @Primary
    @Qualifier("mongo-template-data")
    public MongoTemplate mongoTemplateData() {
        final MongoDatabaseFactory factory = mongoDatabaseFactoryData();
        final MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);

        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(factory, converter);
    }

    @Bean
//    @Primary
    @Qualifier("mongo-template-transaction")
    public MongoTemplate mongoTemplateTransactions() {
    	final MongoDatabaseFactory factory = mongoDatabaseFactoryTransactions();
        final MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);

        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    	return new MongoTemplate(factory, converter);
    }
  
 
}
