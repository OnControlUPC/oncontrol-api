package pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarkProcedureComplianceCommand(
        Long procedureId,
        UUID patientProfileUuid,
        LocalDateTime completionDate
) {
}
