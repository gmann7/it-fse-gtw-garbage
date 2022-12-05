/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import java.util.Date;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save validation events.
 */
@Data
@NoArgsConstructor
@Document(collection = "#{@transactionEventsBean}")
public class TransactionEventsETY {
 
	@Id
	@Size(min = 0, max = 100)
	private String id;
	
	@Field(name = "eventType")
	@Size(min = 0, max = 100)
	private String eventType;

	@Field(name = "eventDate")
	private Date eventDate;  
	
	@Field(name = "eventStatus")
	@Size(min = 0, max = 100)
	private String eventStatus;
	
	@Field(name = "message")
	@Size(min = 0, max = 100)
	private String message;

	@Field(name = "identificativoDocumento")
	@Size(min = 0, max = 100)
	private String identificativoDocumento;

	@Field(name = "subject")
	@Size(min = 0, max = 100)
	private String subject;

	@Field(name = "subjectRole")
	@Size(min = 0, max = 100)
	private String subjectRole;
	
	@Field(name = "tipoAttivita")
	@Size(min = 0, max = 100)
	private String tipoAttivita;
	
	@Field(name = "organizzazione")
	@Size(min = 0, max = 100)
	private String organizzazione;
	
	@Field(name = "workflow_instance_id")
	@Size(min = 0, max = 100)
	private String workflowInstanceId;
	
	@Field(name = "expiring_date")
	private Date expiringDate;  
}
