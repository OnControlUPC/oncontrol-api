package pe.edu.upc.oncontrol.treatment.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.application.acl.ProfileAccessAcl;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureComplianceCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.UpdateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.UpdateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.Procedure;
import pe.edu.upc.oncontrol.treatment.domain.model.entities.SymptomLog;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.*;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentCommandService;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.ProcedureRepository;
import pe.edu.upc.oncontrol.treatment.infrastructure.presistence.jpa.repositories.TreatmentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TreatmentCommandServiceImpl implements TreatmentCommandService {
    private final TreatmentRepository treatmentRepository;
    private final ProcedureRepository procedureRepository;
    private final ProfileAccessAcl profileAccessAcl;

    public TreatmentCommandServiceImpl(TreatmentRepository treatmentRepository, ProcedureRepository procedureRepository, ProfileAccessAcl profileAccessAcl) {
        this.treatmentRepository = treatmentRepository;
        this.procedureRepository = procedureRepository;
        this.profileAccessAcl = profileAccessAcl;
    }

    @Override
    public UUID createTreatment(CreateTreatmentCommand command) {
        UUID doctorUuid = command.doctorProfileUuid();
        UUID patientUuid = command.patientProfileUuid();

        // Validar acceso entre doctor y paciente
        if (!profileAccessAcl.isLinkActive(doctorUuid, patientUuid)) {
            throw new IllegalStateException("El doctor no tiene acceso activo al paciente.");
        }

        // Validar que el doctor no tenga ya un tratamiento activo con ese paciente
        boolean yaExiste = treatmentRepository.existsByDoctorProfileUuidAndPatientProfileUuidAndStatus(
                doctorUuid, patientUuid, TreatmentStatus.ACTIVE
        );
        if (yaExiste) {
            throw new IllegalStateException("Ya existe un tratamiento activo entre este doctor y paciente.");
        }

        // Validar que el paciente no tenga más de 3 tratamientos activos
        long tratamientosActivosPaciente = treatmentRepository.countByPatientProfileUuidAndStatus(
                patientUuid, TreatmentStatus.ACTIVE
        );
        if (tratamientosActivosPaciente >= 3) {
            throw new IllegalStateException("El paciente ya tiene el número máximo de tratamientos activos.");
        }

        // Crear Treatment
        TreatmentTitle title = new TreatmentTitle(command.title());
        TreatmentPeriod period = new TreatmentPeriod(command.startDate(), command.endDate());

        Treatment treatment = new Treatment(title, period, doctorUuid, patientUuid);

        Treatment saved = treatmentRepository.save(treatment);
        return saved.getExternalId();
    }

    @Override
    public void addProcedure(CreateProcedureCommand command) {
        UUID treatmentId = command.treatmentExternalId();

        Treatment treatment = treatmentRepository.findByExternalId(treatmentId)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado."));

        // Validación de propiedad: solo el doctor dueño puede agregar procedimientos
        if (!treatment.getDoctorProfileUuid().equals(command.doctorProfileUuid())) {
            throw new AccessDeniedException("Solo el doctor asignado puede modificar el tratamiento.");
        }

        // Construcción del procedimiento
        ProcedureDescription description = new ProcedureDescription(command.description());

        SchedulePattern pattern = new SchedulePattern(
                command.recurrenceType(),
                command.interval(),
                command.totalOccurrences(),
                command.untilDate()
        );

        Procedure procedure = new Procedure(
                description,
                pattern,
                command.firstExecutionTime(),
                treatment
        );

        treatment.addProcedure(procedure); // asocia al tratamiento

        treatmentRepository.save(treatment); // cascada lo guarda
    }

    @Override
    public void updateTreatment(UpdateTreatmentCommand command) {

    }

    @Override
    public void updateProcedure(UpdateProcedureCommand command) {

    }

    @Override
    public void markProcedureCompliance(MarkProcedureComplianceCommand command) {
        Procedure procedure = procedureRepository
                .findByIdAndTreatment_PatientProfileUuid(command.procedureId(), command.patientProfileUuid())
                .orElseThrow(() -> new EntityNotFoundException("El procedimiento no pertenece al paciente."));

        Treatment treatment = procedure.getTreatment();
        if (treatment.getStatus() != TreatmentStatus.ACTIVE) {
            throw new IllegalStateException("El tratamiento no está activo.");
        }

        LocalDateTime completionDate = command.completionDate();
        LocalDateTime scheduledDate = procedure.getScheduledAt();

        boolean dentroDelMargen = !completionDate.isAfter(scheduledDate.plusDays(1));
        boolean enTiempo = !completionDate.isAfter(scheduledDate);

        if (procedure.getComplianceStatus() != ComplianceStatus.PENDING) {
            throw new IllegalStateException("Este procedimiento ya fue completado o regularizado.");
        }

        if (enTiempo) {
            procedure.markCompleted(completionDate);
        } else if (dentroDelMargen) {
            procedure.regularize(completionDate);
        } else {
            throw new IllegalStateException("El procedimiento no puede marcarse como cumplido fuera del margen permitido.");
        }

        procedureRepository.save(procedure);
    }


    @Override
    public void logSymptom(CreateSymptomCommand command) {
        UUID treatmentExternalId = command.treatmentExternalId();
        UUID patientProfileUuid = command.patientProfileUuid();

        Treatment treatment = treatmentRepository.findByExternalId(treatmentExternalId)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado."));

        if (!treatment.getPatientProfileUuid().equals(patientProfileUuid)) {
            throw new AccessDeniedException("El tratamiento no pertenece al paciente.");
        }

        if (treatment.getStatus() != TreatmentStatus.ACTIVE) {
            throw new IllegalStateException("No se pueden registrar síntomas en un tratamiento inactivo.");
        }

        SymptomLog log = new SymptomLog(
                command.loggedAt(),
                command.symptomType(),
                command.severity(),
                command.notes(),
                treatment,
                patientProfileUuid
        );

        treatment.logSymptom(log);
        treatmentRepository.save(treatment); // cascada guarda log
    }

}
