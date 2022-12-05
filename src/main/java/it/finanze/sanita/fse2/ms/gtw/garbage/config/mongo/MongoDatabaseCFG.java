/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;

@Configuration
public class MongoDatabaseCFG {

	@Autowired
	private MongoPropertiesDataCFG mongoData;

	@Autowired
	private MongoPropertiesTransactionsCFG mongoTransactions;

	@Autowired
	private MongoPropertiesValidatedDocumentCFG mongoFse;

	@Autowired
	private ApplicationContext appContext;

	final List<Converter<?, ?>> conversions = new ArrayList<>();

	@Bean
	@Primary
	@Qualifier("mongo-factory-data")
	public MongoDatabaseFactory mongoDatabaseFactoryData() {
		return new SimpleMongoClientDatabaseFactory(mongoData.getUri());
	}

	@Bean
	@Qualifier("mongo-factory-transaction")
	public MongoDatabaseFactory mongoDatabaseFactoryTransactions() {
		return new SimpleMongoClientDatabaseFactory(mongoTransactions.getUri());
	}

	@Bean
	@Qualifier("mongo-factory-valdoc")
	public MongoDatabaseFactory mongoDatabaseFactoryFse() {
		return new SimpleMongoClientDatabaseFactory(mongoFse.getUri());
	}

	@Bean
	@Primary
	@Qualifier("mongo-template-data")
	public MongoTemplate mongoTemplateData() {
		final MongoDatabaseFactory factory = mongoDatabaseFactoryData();
		final MongoMappingContext mongoMappingContext = new MongoMappingContext();
		mongoMappingContext.setApplicationContext(appContext);

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				mongoMappingContext);
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

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(factory, converter);
	}

	@Bean
	@Qualifier("mongo-template-valdoc")
	public MongoTemplate mongoTemplateFse() {
		final MongoDatabaseFactory factory = mongoDatabaseFactoryFse();
		final MongoMappingContext mongoMappingContext = new MongoMappingContext();
		mongoMappingContext.setApplicationContext(appContext);

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(factory, converter);
	}

	@Bean
	@Qualifier("mongo-factory-rules")
	public MongoDatabaseFactory mongoDatabaseFactoryRules() {
		return new SimpleMongoClientDatabaseFactory(mongoFse.getUri());
	}

	@Bean
	@Qualifier("mongo-template-rules")
	public MongoTemplate mongoTemplateRules() {
		final MongoDatabaseFactory factory = mongoDatabaseFactoryFse();
		final MongoMappingContext mongoMappingContext = new MongoMappingContext();
		mongoMappingContext.setApplicationContext(appContext);

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),
				mongoMappingContext);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(factory, converter);
	}
	
	@Bean
	public LockProvider lockProvider(MongoTemplate template) {
		return new MongoLockProvider(template.getDb());
	}

}
