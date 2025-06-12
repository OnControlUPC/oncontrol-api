package pe.edu.upc.oncontrol.treatment.application.internal.queryservice;

import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ComplianceStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.TreatmentStatus;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentQueryService;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.SymptomLogRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.TreatmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TreatmentQueryServiceImpl implements TreatmentQueryService {

    private final TreatmentRepository treatmentRepository;
    private final ProcedureRepository procedureRepository;
    private final SymptomLogRepository symptomLogRepository;

    public TreatmentQueryServiceImpl(TreatmentRepository treatmentRepository, ProcedureRepository procedureRepository, SymptomLogRepository symptomLogRepository) {
        this.treatmentRepository = treatmentRepository;
        this.procedureRepository = procedureRepository;
        this.symptomLogRepository = symptomLogRepository;
    }

    @Override
    public Optional<Treatment> getTreatmentByExternalId(UUID treatmentExternalId) {
        return treatmentRepository.findByExternalId(treatmentExternalId);
    }

    @Override
    public List<Treatment> getActiveTreatmentsByPatient(UUID patientProfileUuid) {
        return treatmentRepository.findAll().stream()
                .filter(t -> t.getPatientProfileUuid().equals(patientProfileUuid)
                        && t.getStatus() == TreatmentStatus.ACTIVE)
                .toList();
    }

    @Override
    public List<Treatment> getActiveTreatmentsByDoctor(UUID doctorProfileUuid) {
        return treatmentRepository.findAll().stream()
                .filter(t -> t.getDoctorProfileUuid().equals(doctorProfileUuid)
                        && t.getStatus() == TreatmentStatus.ACTIVE)
                .toList();
    }

    @Override
    public List<Procedure> getPendingProceduresByPatient(UUID patientProfileUuid) {
        return procedureRepository.findByTreatment_PatientProfileUuidAndComplianceStatus(
                patientProfileUuid,
                ComplianceStatus.PENDING
        );
    }

    @Override
    public List<SymptomLog> getSymptomLogsByTreatment(UUID treatmentExternalId) {
        return symptomLogRepository.findByTreatment_ExternalIdOrderByLoggedAtDesc(treatmentExternalId);
    }
}
