package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import it.finanze.sanita.fse2.ms.gtw.garbage.enums.IniEventType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "#{@auditIni}")
@Data
public class AuditIniETY {

    public static final String WORKFLOW_INSTANCE_ID = "workflow_instance_id";
    public static final String EVENT_TYPE = "eventType";
    public static final String EVENT_DATE = "eventDate";
    public static final String MICROSERVICE_NAME = "microserviceName";
    public static final String SOAP_REQUEST = "soapRequest";
    public static final String SOAP_RESPONSE = "soapResponse";
    public static final String EXPIRING_DATE = "expiring_date";

    @Id
    private String id;

    @Field(name = WORKFLOW_INSTANCE_ID)
    private final String workflowInstanceId;

    @Field(name = EVENT_TYPE)
    private final IniEventType eventType;

    @Field(name = EVENT_DATE)
    private final Date eventDate;

    @Field(name = SOAP_REQUEST)
    private String soapRequest;

    @Field(name = SOAP_RESPONSE)
    private String soapResponse;

    @Field(name = MICROSERVICE_NAME)
    private String microserviceName;

    @Field(name = EXPIRING_DATE)
    private Date expiringDate;

    public AuditIniETY(String workflowInstanceId, IniEventType eventType, Date eventDate, String soapRequest, String soapResponse){
        this.workflowInstanceId = workflowInstanceId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.soapRequest = soapRequest;
        this.soapResponse = soapResponse;
    }

}
