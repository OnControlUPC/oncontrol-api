package pe.edu.upc.oncontrol.treatment.interfaces;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureComplianceCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentCommandService;
import pe.edu.upc.oncontrol.treatment.domain.services.treatment.TreatmentQueryService;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.AddProcedureResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.CreateTreatmentResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.LogSymptomResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.MarkProcedureComplianceResource;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.AddProcedureAssembler;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.CreateTreatmentAssembler;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.LogSymptomAssembler;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.transform.MarkProcedureComplianceAssembler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/treatments", produces = MediaType.APPLICATION_JSON_VALUE)
public class TreatmentController {

    private final TreatmentCommandService treatmentService;
    private final TreatmentQueryService treatmentQueryService;
    private final CreateTreatmentAssembler createTreatmentAssembler;
    private final AddProcedureAssembler addProcedureAssembler;
    private final MarkProcedureComplianceAssembler markProcedureComplianceAssembler;
    private final LogSymptomAssembler logSymptomAssembler;

    public TreatmentController(TreatmentCommandService treatmentService,
                               TreatmentQueryService treatmentQueryService,
                               CreateTreatmentAssembler createTreatmentAssembler,
                               AddProcedureAssembler addProcedureAssembler,
                               MarkProcedureComplianceAssembler markProcedureComplianceAssembler,
                               LogSymptomAssembler logSymptomAssembler) {
        this.treatmentService = treatmentService;
        this.treatmentQueryService = treatmentQueryService;
        this.createTreatmentAssembler = createTreatmentAssembler;
        this.addProcedureAssembler = addProcedureAssembler;
        this.markProcedureComplianceAssembler = markProcedureComplianceAssembler;
        this.logSymptomAssembler = logSymptomAssembler;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateTreatmentResource resource) {
        CreateTreatmentCommand command = createTreatmentAssembler.toCommand(resource);
        UUID externalId = treatmentService.createTreatment(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(externalId);
    }

    @PostMapping("/{treatmentId}/procedures")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> addProcedure(@PathVariable UUID treatmentId,
                                             @Valid @RequestBody AddProcedureResource resource) {
        CreateProcedureCommand command = addProcedureAssembler.toCommand(treatmentId, resource);
        treatmentService.addProcedure(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/procedures/{procedureId}/compliance")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> markCompliance(@PathVariable Long procedureId,
                                               @Valid @RequestBody MarkProcedureComplianceResource resource) {
        MarkProcedureComplianceCommand command = markProcedureComplianceAssembler.toCommand(procedureId, resource);
        treatmentService.markProcedureCompliance(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{treatmentId}/symptoms")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<Void> logSymptom(@PathVariable UUID treatmentId,
                                           @Valid @RequestBody LogSymptomResource resource) {
        CreateSymptomCommand command = logSymptomAssembler.toCommand(treatmentId, resource);
        treatmentService.logSymptom(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{externalId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATIENT', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Treatment> getByExternalId(@PathVariable UUID externalId) {
        return treatmentQueryService.getTreatmentByExternalId(externalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/doctor/{doctorUuid}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<Treatment>> getByDoctor(@PathVariable UUID doctorUuid) {
        List<Treatment> treatments = treatmentQueryService.getActiveTreatmentsByDoctor(doctorUuid);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/patient/{patientUuid}")
    @PreAuthorize("hasAnyRole('ROLE_PATIENT', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<Treatment>> getByPatient(@PathVariable UUID patientUuid) {
        List<Treatment> treatments = treatmentQueryService.getActiveTreatmentsByPatient(patientUuid);
        return ResponseEntity.ok(treatments);
    }


}

