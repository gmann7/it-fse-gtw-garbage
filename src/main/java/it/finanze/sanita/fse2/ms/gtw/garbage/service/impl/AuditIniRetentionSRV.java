package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IAuditIniRetentionRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IAuditIniRetentionSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditIniRetentionSRV implements IAuditIniRetentionSRV{

    @Autowired
    IAuditIniRetentionRepo repo;

    @Override
    public void deleteAudit(Date date) {
        repo.deleteExpiredAudit(date);
    }
}
