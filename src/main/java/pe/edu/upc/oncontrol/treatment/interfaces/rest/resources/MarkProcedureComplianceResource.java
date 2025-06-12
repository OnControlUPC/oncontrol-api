package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarkProcedureComplianceResource(
        UUID patientProfileUuid,
        LocalDateTime completionDate
) {
}
