package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save schema documents
 */
@Document(collection = "#{@schemaBean}")
@Data
@NoArgsConstructor
public class SchemaETY {

	@Id
	private String id;
	
	@Field(name = "name_schema")
	private String nameSchema;
	
	@Field(name = "content_schema")
	private Binary contentSchema;
	
	@Field(name = "type_id_extension")
	private String typeIdExtension;
	
	@Field(name = "root_schema")
	private Boolean rootSchema;
	
	@Field(name = "insertion_date")
	private Date insertionDate;
	
	@Field(name = "last_update")
	private Date lastUpdateDate;
	
	@Field(name = "deleted")
	private boolean deleted;
   
}