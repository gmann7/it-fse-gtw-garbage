package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.util.Date;

public interface IAuditIniRetentionRepo {

    void deleteExpiredAudit(Date date);
}
