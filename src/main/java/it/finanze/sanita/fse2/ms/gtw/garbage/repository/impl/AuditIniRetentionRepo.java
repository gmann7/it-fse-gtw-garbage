package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IAuditIniRetentionRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.AuditIniETY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class AuditIniRetentionRepo implements IAuditIniRetentionRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${retention.transactions-query.limit}")
    private Integer retentionQueryLimit;

    @Override
    public void deleteExpiredAudit(Date date) {
        Pageable pageable = PageRequest.of(0, retentionQueryLimit);
        Query query = new Query(Criteria.where(AuditIniETY.EXPIRING_DATE).lte(date)).with(pageable);

        List<AuditIniETY> audits;
        int totalDeleted=0;

        do {
            audits = mongoTemplate.find(query, AuditIniETY.class);

            int deletedInBatch=audits.size();
            totalDeleted += deletedInBatch;

            if (!audits.isEmpty()) {
                mongoTemplate.remove(query, AuditIniETY.class);
            }

            pageable = pageable.next();
            query.with(pageable);

            log.info("{} records has been deleted in the current batch", deletedInBatch);

        } while (!audits.isEmpty());

        log.info("Total records deleted: {}", totalDeleted);
    }


}
