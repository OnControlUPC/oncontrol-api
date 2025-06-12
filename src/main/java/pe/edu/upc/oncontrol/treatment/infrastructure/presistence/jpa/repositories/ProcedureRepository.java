package pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ComplianceStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    List<Procedure> findByTreatment_ExternalId(UUID treatmentExternalId);
    List<Procedure> findByTreatment_PatientProfileUuidAndComplianceStatus(
            UUID patientProfileUuid, ComplianceStatus status);
    Optional<Procedure> findByIdAndTreatment_PatientProfileUuid(Long id, UUID patientProfileUuid);
    @Query("SELECT p FROM Procedure p WHERE p.scheduledAt BETWEEN :start AND :end AND p.complianceStatus = 'PENDING'")
    List<Procedure> findPendingInRange(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);
}

