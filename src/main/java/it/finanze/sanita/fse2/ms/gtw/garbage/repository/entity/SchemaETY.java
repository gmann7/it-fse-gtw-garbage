package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save schema documents
 */
@Document(collection = "#{@schemaBean}")
@Data
@NoArgsConstructor
public class SchemaETY {

	public static final String FIELD_ID = "_id";
	public static final String FIELD_FILENAME = "name_schema";
	public static final String FIELD_CONTENT = "content_schema";
	public static final String FIELD_TYPE_ID_EXT = "type_id_extension";
	public static final String FIELD_ROOT_SCHEMA = "root_schema";

	@Id
	private String id;
	@Field(name = FIELD_FILENAME)
	private String nameSchema;
	@Field(name = FIELD_CONTENT)
	private Binary contentSchema;
	@Field(name = FIELD_TYPE_ID_EXT)
	private String typeIdExtension;
	@Field(name = FIELD_ROOT_SCHEMA)
	private Boolean rootSchema;
	@Field(name = "insertion_date")
	private Date insertionDate;
	@Field(name = "last_update")
	private Date lastUpdateDate;
	@Field(name = "deleted")
	private boolean deleted;

	public void setContentSchema(MultipartFile file) throws DataProcessingException {
		try {
			this.contentSchema = new Binary(file.getBytes());
		} catch (IOException e) {
			throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
		}
	}

	public void setContentSchema(Path path) throws DataProcessingException {
		try {
			this.contentSchema = new Binary(Files.readAllBytes(path));
		} catch (IOException e) {
			throw new DataProcessingException("Unable to encode file raw bytes into entity data", e);
		}
	}

	public static SchemaETY fromMultipart(MultipartFile file, String extension, boolean root)
			throws DataProcessingException {
		SchemaETY entity = new SchemaETY();
		Date now = new Date();
		entity.setNameSchema(file.getOriginalFilename());
		entity.setContentSchema(file);
		entity.setTypeIdExtension(extension);
		entity.setRootSchema(root);
		entity.setInsertionDate(now);
		entity.setLastUpdateDate(now);
		return entity;
	}

	public static SchemaETY fromPath(Path path, String extension, boolean root) throws DataProcessingException {
		SchemaETY entity = new SchemaETY();
		Date now = new Date();
		entity.setNameSchema(path.getFileName().toString());
		entity.setContentSchema(path);
		entity.setTypeIdExtension(extension);
		entity.setRootSchema(root);
		entity.setInsertionDate(now);
		entity.setLastUpdateDate(now);
		return entity;
	}
}